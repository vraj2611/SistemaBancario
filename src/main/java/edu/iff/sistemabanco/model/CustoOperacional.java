package edu.iff.sistemabanco.model;

import java.util.Calendar;

import javax.persistence.Entity;

@Entity
public class CustoOperacional extends Transacao {

	private static final long serialVersionUID = 1L;

	public CustoOperacional() {}
	
	public static CustoOperacional criar(Conta conta, double valor, Operador o, String descricao) {
		CustoOperacional t = new CustoOperacional();
		t.setStatus(StatusTransacaoEnum.AUTORIZADA);
		t.setTipo(TipoTransacaoEnum.CUSTO);
		t.setOperador(o);
		t.setConta(conta);
		t.setDescricao(descricao);
		t.setValor(Math.ceil(valor * 100) / 100);
		Calendar data = Calendar.getInstance();
		t.setData_criacao(data);
		t.setData_operador(data);
		return t;
	}
	
	@Override
	public double getValorParaSaldo(Conta c) {
		return -this.getValor();
	}

}
