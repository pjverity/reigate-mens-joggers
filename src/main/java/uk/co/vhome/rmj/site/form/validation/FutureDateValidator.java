package uk.co.vhome.rmj.site.form.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FutureDateValidator implements ConstraintValidator<NotPastLocalDate, LocalDate>
{
	@Override
	public void initialize(NotPastLocalDate notPastLocalDate)
	{

	}

	@Override
	public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext)
	{
		return localDate == null || !localDate.isBefore(LocalDate.now());
	}
}
