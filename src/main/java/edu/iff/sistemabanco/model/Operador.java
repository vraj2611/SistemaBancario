package edu.iff.sistemabanco.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Operador extends Pessoa {

	private static final long serialVersionUID = 1L;

	@JsonIgnoreProperties({"conta","conta_destino","operador","data_criacao","hibernateLazyInitializer", "handler"})
	@OneToMany(mappedBy = "operador", cascade = CascadeType.REMOVE, orphanRemoval = false)
	private List<Transacao> transacoes = new ArrayList<Transacao>();

	public Operador() {
	}

	public List<Transacao> getTransacoes() {
		return transacoes;
	}

	public void setTransacoes(List<Transacao> transacoes) {
		this.transacoes = transacoes;
	}

}
