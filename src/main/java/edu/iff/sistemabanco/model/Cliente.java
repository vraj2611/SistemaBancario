package edu.iff.sistemabanco.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(value = "activation_code", allowGetters = false, allowSetters = true)
public class Cliente extends Pessoa {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnoreProperties({"cliente","transacoes","pacote"})
	@OneToMany(mappedBy="cliente", cascade = CascadeType.REMOVE, orphanRemoval = false)
	private List<Conta> contas = new ArrayList<Conta>();

	@Column(nullable = false)
	private boolean ativo;
	
	public Cliente() {}

	public List<Conta> getContas() {
		return contas;
	}

	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	
}
