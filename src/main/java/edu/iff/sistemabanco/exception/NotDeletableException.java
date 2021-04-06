package edu.iff.sistemabanco.exception;

public class NotDeletableException  extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NotDeletableException(String msg) {
		super(msg);
	}

}