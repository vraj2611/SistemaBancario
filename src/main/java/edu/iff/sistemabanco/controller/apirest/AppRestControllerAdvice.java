package edu.iff.sistemabanco.controller.apirest;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import edu.iff.sistemabanco.exception.Error;
import edu.iff.sistemabanco.exception.NotDeletableException;
import edu.iff.sistemabanco.exception.NotFoundException;
import edu.iff.sistemabanco.exception.PropertyError;
import edu.iff.sistemabanco.exception.SaldoIndisponivelException;
import edu.iff.sistemabanco.exception.ValidationError;

@RestControllerAdvice
public class AppRestControllerAdvice {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Error> erroPadrao(Exception e, HttpServletRequest request) {
		Error erro = new Error(
			Calendar.getInstance(),
			HttpStatus.BAD_REQUEST.value(),
			HttpStatus.BAD_REQUEST.name(),
			e.getMessage(),
			request.getRequestURI()
				);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Error> erroPadrao(NotFoundException e, HttpServletRequest request) {
		Error erro = new Error(
			Calendar.getInstance(),
			HttpStatus.NOT_FOUND.value(),
			HttpStatus.NOT_FOUND.name(),
			e.getMessage(),
			request.getRequestURI()
				);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
	
	@ExceptionHandler(SaldoIndisponivelException.class)
	public ResponseEntity<Error> erroPadrao(SaldoIndisponivelException e, HttpServletRequest request) {
		Error erro = new Error(
			Calendar.getInstance(),
			HttpStatus.FORBIDDEN.value(),
			HttpStatus.FORBIDDEN.name(),
			e.getMessage(),
			request.getRequestURI()
				);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
	
	@ExceptionHandler(NotDeletableException.class)
	public ResponseEntity<Error> erroPadrao(NotDeletableException e, HttpServletRequest request) {
		Error erro = new Error(
			Calendar.getInstance(),
			HttpStatus.FORBIDDEN.value(),
			HttpStatus.FORBIDDEN.name(),
			e.getMessage(),
			request.getRequestURI()
				);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
	}

	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Error> erroValidacao(ConstraintViolationException e, HttpServletRequest request) {
		ValidationError erro = new ValidationError(
			Calendar.getInstance(),
			HttpStatus.UNPROCESSABLE_ENTITY.value(),
			HttpStatus.UNPROCESSABLE_ENTITY.name(),
			e.getMessage(),
			request.getRequestURI()
				);
		for(ConstraintViolation<?> cv: e.getConstraintViolations()) {
			PropertyError p = new PropertyError(cv.getPropertyPath().toString(), cv.getMessage());
			erro.getErrors().add(p);
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Error> erroValidacao(MethodArgumentNotValidException e, HttpServletRequest request) {
		ValidationError erro = new ValidationError(
			Calendar.getInstance(),
			HttpStatus.UNPROCESSABLE_ENTITY.value(),
			HttpStatus.UNPROCESSABLE_ENTITY.name(),
			"Erro de Validação!",
			request.getRequestURI()
				);
		for(FieldError fe: e.getBindingResult().getFieldErrors()) {
			PropertyError p = new PropertyError(fe.getField(), fe.getDefaultMessage());
			erro.getErrors().add(p);
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
	}
}
