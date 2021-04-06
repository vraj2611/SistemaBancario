package edu.iff.sistemabanco.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Cliente extends Pessoa {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnoreProperties({"cliente","transacoes","pacote"})
	@OneToMany(mappedBy="cliente", cascade = CascadeType.REMOVE, orphanRemoval = false)
	private List<Conta> contas = new ArrayList<Conta>();

	public Cliente() {}

	public List<Conta> getContas() {
		return contas;
	}

	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}
	
	
}
