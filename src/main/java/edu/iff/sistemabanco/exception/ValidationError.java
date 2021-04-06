package edu.iff.sistemabanco.exception;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.validation.ConstraintViolationException;

public class ValidationError extends Error {

	private static final long serialVersionUID = 1L;

	private List<PropertyError> errors = new ArrayList<>(); 
	
	public ValidationError(Calendar timestamp, Integer status, String error, String message, String path) {
		super(timestamp, status, error, message, path);
	}

	public List<PropertyError> getErrors() {
		return errors;
	}

	public void setErrors(List<PropertyError> errors) {
		this.errors = errors;
	}
 
	public static void isConstraintViolation(Exception e) {
		Throwable t = e;
		while(t.getCause() != null) {
			t = t.getCause();
			if( t instanceof ConstraintViolationException) {
				throw ((ConstraintViolationException) t);
			}
		}	
	} 
	
	

}
