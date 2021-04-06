package edu.iff.sistemabanco.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class PacoteServico {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50, unique=true)
	private String nome;
	
	@Column(precision=16, scale=2, nullable = false)
	private double valor_mensalidade;
	
	@Column(precision=16, scale=2, nullable = false)
	private double taxa_rendimento;
	
	@Column(nullable = false)
	private int qtde_retiradas;
	
	@Column(precision=16, scale=2, nullable = false)
	private double valor_retiradas;
	
	@Column(nullable = false)
	@Min(1) @Max(25)
	private int dia_vencimento;

	@Column(nullable = false)
	@Min(0) @Max(99999)
	private int limite_preaprovado;
	
	@JsonIgnoreProperties({"transacoes","pacote","cliente"})
	@OneToMany(mappedBy="pacote", cascade = CascadeType.REMOVE, orphanRemoval = false)
	private List<Conta> contas = new ArrayList<Conta>();
	
	public PacoteServico() {
		// TODO Auto-generated constructor stub
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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	public double getValor_mensalidade() {
		return valor_mensalidade;
	}

	public void setValor_mensalidade(double valor_mensalidade) {
		this.valor_mensalidade = valor_mensalidade;
	}

	public double getTaxa_rendimento() {
		return taxa_rendimento;
	}

	public void setTaxa_rendimento(double taxa_rendimento) {
		this.taxa_rendimento = taxa_rendimento;
	}

	public int getQtde_retiradas() {
		return qtde_retiradas;
	}

	public void setQtde_retiradas(int qtde_retiradas) {
		this.qtde_retiradas = qtde_retiradas;
	}

	public double getValor_retiradas() {
		return valor_retiradas;
	}

	public void setValor_retiradas(double valor_retiradas) {
		this.valor_retiradas = valor_retiradas;
	}

	public int getDia_vencimento() {
		return dia_vencimento;
	}

	public void setDia_vencimento(int dia_vencimento) {
		this.dia_vencimento = dia_vencimento;
	}

	
	public int getLimite_preaprovado() {
		return limite_preaprovado;
	}

	public void setLimite_preaprovado(int limite_preaprovado) {
		this.limite_preaprovado = limite_preaprovado;
	}

	public List<Conta> getContas() {
		return contas;
	}

	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		PacoteServico other = (PacoteServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}


	
}
