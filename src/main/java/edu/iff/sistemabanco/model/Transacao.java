package edu.iff.sistemabanco.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.iff.sistemabanco.annotation.OperadorTransacaoValidation;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@OperadorTransacaoValidation
public abstract class Transacao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(precision = 16, scale = 2, nullable = false)
	@Positive()
	private double valor;

	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Tipo de transação obrigatório.")
	private TipoTransacaoEnum tipo;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "Data de transação é obrigatorio!")
	@PastOrPresent(message = "Data de transação não pode ser no futuro!")
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	@JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	private Calendar data_criacao;

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent(message = "Data de transação não pode ser no futuro!")
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	@JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	private Calendar data_operador;

	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private StatusTransacaoEnum status;

	@Column(nullable = true, length = 50)
	@Length(max = 50, message = "Descricao deve ter no maximo 50 caracteres")
	private String descricao;

	@JsonIgnoreProperties({ "transacoes", "pacote", "hibernateLazyInitializer" })
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "Conta é obrigatorio!")
	private Conta conta;

	@JsonIgnoreProperties({ "transacoes", "hibernateLazyInitializer" })
	@ManyToOne(fetch = FetchType.LAZY)
	private Operador operador;
	
	private void checkTransacaoPendente() {
		if (this.getStatus() != StatusTransacaoEnum.PENDENTE) {
			String msg = "Essa transacao ja foi " + this.getStatus();
			if (this.getStatus() != StatusTransacaoEnum.PREAPROVADA)
				msg += " por " + this.getOperador().getNome();
			throw new RuntimeException(msg);
		}
	}

	public void autorizar(Operador o) {
		this.checkTransacaoPendente();
		this.setOperador(o);
		this.setData_operador(Calendar.getInstance());
		this.setStatus(StatusTransacaoEnum.AUTORIZADA);
	}

	public void bloquear(Operador o) {
		this.checkTransacaoPendente();
		this.setOperador(o);
		this.setData_operador(Calendar.getInstance());
		this.setStatus(StatusTransacaoEnum.BLOQUEADA);
	}

	public double getValorParaSaldo(Conta c) {
		throw new RuntimeException("Implemente esse metodo na subclasse!");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

	public Calendar getData_criacao() {
		return data_criacao;
	}

	public void setData_criacao(Calendar data_criacao) {
		this.data_criacao = data_criacao;
	}

	public Calendar getData_operador() {
		return data_operador;
	}

	public void setData_operador(Calendar data_operador) {
		this.data_operador = data_operador;
	}

	public TipoTransacaoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoTransacaoEnum tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public StatusTransacaoEnum getStatus() {
		return status;
	}

	public void setStatus(StatusTransacaoEnum status) {
		this.status = status;
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
		Transacao other = (Transacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
