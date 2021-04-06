package edu.iff.sistemabanco.model;

public class ContaDto {

	private Long cliente_id;
	private String nome;
	private Long pacote_id;

	public ContaDto(Long cliente_id, String nome, Long pacote_id) {
		this.cliente_id = cliente_id;
		this.nome = nome;
		this.pacote_id = pacote_id;
	}

	public Long getCliente_id() {
		return cliente_id;
	}

	public void setCliente_id(Long cliente_id) {
		this.cliente_id = cliente_id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getPacote_id() {
		return pacote_id;
	}

	public void setPacote_id(Long pacote_id) {
		this.pacote_id = pacote_id;
	}

}
