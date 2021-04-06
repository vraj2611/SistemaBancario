package edu.iff.sistemabanco.model;

import java.util.Calendar;

import javax.persistence.Entity;

@Entity
public class Retirada extends Transacao {

	private static final long serialVersionUID = 1L;
	
	public Retirada() {	}

	public static Retirada criar(Conta conta, double valor, String descricao) {
		Retirada t = new Retirada();
		t.setTipo(TipoTransacaoEnum.RETIRADA);
		t.setConta(conta);
		t.setDescricao(descricao);
		t.setValor(Math.ceil(valor * 100) / 100);
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
		return this.getValor()*(-1);
	}
}
