package edu.iff.sistemabanco.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Transferencia extends Transacao {

	private static final long serialVersionUID = 1L;

	@ManyToOne()
    @JsonIgnoreProperties({"transacoes","pacote","cliente","hibernateLazyInitializer"})
	@JoinColumn(name = "conta_destino")
	@NotNull(message = "Deve ter uma conta destino!")
	private Conta conta_destino;

	public Transferencia() {
	}

	public static Transferencia criar(Conta conta, double valor, Conta conta_destino, String descricao) {
		Transferencia t = new Transferencia();
		t.setTipo(TipoTransacaoEnum.TRANSFERENCIA);
		t.setConta(conta);
		t.setValor(Math.ceil(valor * 100) / 100);
		t.setConta_destino(conta_destino);
		t.setDescricao(descricao);
		t.setData_criacao(Calendar.getInstance());
		if(valor <= conta.getPacote().getLimite_preaprovado()) {
			t.setStatus(StatusTransacaoEnum.PREAPROVADA);
		} else {
			t.setStatus(StatusTransacaoEnum.PENDENTE);
		}
		return t;
	}

	@Override
	public double getValorParaSaldo(Conta c) {
		if (this.getConta().equals(c))
			return -this.getValor();
		if (this.getConta_destino().equals(c))
			return this.getValor();
		throw new RuntimeException("Transferencia nao pertence a essa conta");
	}

	public Conta getConta_destino() {
		return conta_destino;
	}

	public void setConta_destino(Conta conta_destino) {
		this.conta_destino = conta_destino;
	}

}
