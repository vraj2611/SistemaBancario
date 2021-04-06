package edu.iff.sistemabanco.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iff.sistemabanco.model.StatusTransacaoEnum;
import edu.iff.sistemabanco.model.Transacao;

public class OperadorTransacaoValidator implements ConstraintValidator<OperadorTransacaoValidation, Transacao> {

	@Override
	public boolean isValid(Transacao value, ConstraintValidatorContext context) {
		if (value == null)
			return false;
		if (value.getOperador() == null) {
			if (value.getStatus() == StatusTransacaoEnum.BLOQUEADA)
				return false;
			if (value.getStatus() == StatusTransacaoEnum.AUTORIZADA)
				return false;
		} else {
			if (value.getStatus() == StatusTransacaoEnum.PENDENTE)
				return false;
			if (value.getStatus() == StatusTransacaoEnum.PREAPROVADA)
				return false;
		}
		return true;
	}

}
