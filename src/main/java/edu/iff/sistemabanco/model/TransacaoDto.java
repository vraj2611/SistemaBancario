package edu.iff.sistemabanco.model;

public class TransacaoDto {
	private double valor;
	private String descricao;
	private Long id_conta_destino;
	private String cpf_conta_destino;

	public TransacaoDto(double valor, String descricao, Long id_conta_destino,
			String cpf_conta_destino) {
		this.valor = valor;
		this.descricao = descricao;
		this.id_conta_destino = id_conta_destino;
		this.cpf_conta_destino = cpf_conta_destino;
	}

	public double getValor() {
		return valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public Long getId_conta_destino() {
		return id_conta_destino;
	}

	public String getCpf_conta_destino() {
		return cpf_conta_destino;
	}

}
