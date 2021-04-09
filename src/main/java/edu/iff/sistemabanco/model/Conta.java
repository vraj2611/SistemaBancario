package edu.iff.sistemabanco.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.iff.sistemabanco.exception.SaldoIndisponivelException;

@Entity
public class Conta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	@NotBlank(message = "Nome da Conta nao pode estar vazio!")
	@Length(max = 50, message = "Nome da Conta deve ter no maximo 50 caracteres")
	private String nome;

	@Column(precision = 16, scale = 2, nullable = false)
	@Digits(integer = 16, fraction = 2, message = "Saldo deve ter 2 casas decimais")
	private double saldo;

	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent(message = "Momento do Saldo não pode ser no futuro")
	@NotNull(message = "Data do saldo da conta é obrigatorio!")
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	@JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	private Calendar datahora;

	@JsonIgnore
	@OneToMany(mappedBy = "conta", cascade = CascadeType.REMOVE, orphanRemoval = false)
	private List<Transacao> transacoes = new ArrayList<Transacao>();

	@JsonIgnoreProperties({ "contas", "hibernateLazyInitializer" })
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "Conta deve ter um pacote de servico associado!")
	private PacoteServico pacote;

	@JsonIgnoreProperties({ "contas", "hibernateLazyInitializer" })
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "Conta deve ter um cliente associado!")
	private Cliente cliente;

	public Conta() {
	}

	public static Conta criar(Cliente c, String n, PacoteServico p) {
		Conta conta = new Conta();
		conta.setCliente(c);
		conta.setNome(n);
		conta.setPacote(p);
		conta.setSaldo(0);
		conta.setDatahora(Calendar.getInstance());
		return conta;
	}

	public void atualizarSaldo() {
		double atual = 0;
		for (Transacao t : this.getTransacoes()) {
			if (t.getStatus() == StatusTransacaoEnum.PENDENTE) continue;
			if (t.getStatus() == StatusTransacaoEnum.BLOQUEADA) continue;
			atual = atual + t.getValorParaSaldo(this);
		}
		this.setSaldo(Math.ceil(100 * atual) / 100);
		this.setDatahora(Calendar.getInstance());
	}

	private void checkSaldoDisponivel(double valor) {
		this.atualizarSaldo();
		if (valor > this.getSaldo()) {
			String msg = "A conta "+this.getId()+" não tem saldo disponivel (" + this.getSaldo();
			msg += ") para efetuar a transacao no valor de " + valor + "!";
			throw new SaldoIndisponivelException(msg);
		}
	}

	public Deposito depositar(double valor, String descricao) {
		Deposito d = Deposito.criar(this, valor, descricao);
		this.transacoes.add(d);
		this.atualizarSaldo();
		return d;
	}

	public Retirada retirar(double valor, String descricao) {
		this.checkSaldoDisponivel(valor);
		Retirada r = Retirada.criar(this, valor, descricao);
		this.transacoes.add(r);
		return r;
	}

	public Transferencia transferir(double valor, Conta conta_destino, String descricao) {
		this.checkSaldoDisponivel(valor);
		Transferencia t = Transferencia.criar(this, valor, conta_destino, descricao);
		this.transacoes.add(t);
		return t;
	}

	public int calcularQuantidadeRetiradas() {
		int qtd = 0;
		for (Transacao t : this.getTransacoes()) {
			if (t.getTipo() == TipoTransacaoEnum.RETIRADA)
				qtd += 1;
		}
		return qtd;
	}

	public double calcularCustosOperacionais() {
		PacoteServico p = this.getPacote();
		double custo = p.getValor_mensalidade();
		int qtde_retiradas = this.calcularQuantidadeRetiradas();
		if (qtde_retiradas > p.getQtde_retiradas())
			custo = +p.getValor_retiradas() * (qtde_retiradas - p.getQtde_retiradas());
		return custo;
	}

	public CustoOperacional debitarCustos(Operador o, String descricao) {
		double valor = this.calcularCustosOperacionais();
		CustoOperacional c = CustoOperacional.criar(this, valor, o, descricao);
		this.transacoes.add(c);
		this.atualizarSaldo();
		return c;
	}

	public double calcularRendimentos() {
		this.atualizarSaldo();
		return this.getSaldo() * this.getPacote().getTaxa_rendimento();
	}

	public Rendimento pagarRendimentos(Operador o, String descricao) {
		double valor = this.calcularRendimentos();
		Rendimento r = Rendimento.criar(this, valor, o, descricao);
		this.transacoes.add(r);
		this.atualizarSaldo();
		return r;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public Calendar getDatahora() {
		return datahora;
	}

	public void setDatahora(Calendar datahora) {
		this.datahora = datahora;
	}

	public List<Transacao> getTransacoes() {
		return transacoes;
	}

	public void setTransacoes(List<Transacao> transacoes) {
		this.transacoes = transacoes;
	}

	public PacoteServico getPacote() {
		return pacote;
	}

	public void setPacote(PacoteServico pacote) {
		this.pacote = pacote;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conta other = (Conta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
