package edu.iff.sistemabanco.exception;

public class SaldoIndisponivelException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public SaldoIndisponivelException(String msg) {
		super(msg);
	}

}