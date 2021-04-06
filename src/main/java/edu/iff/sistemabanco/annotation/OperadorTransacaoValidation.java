package edu.iff.sistemabanco.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy=OperadorTransacaoValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperadorTransacaoValidation {

	String message() default "Transacao autorizada deve ter Operador";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
