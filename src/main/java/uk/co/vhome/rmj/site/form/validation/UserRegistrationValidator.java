package uk.co.vhome.rmj.site.form.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.vhome.rmj.services.ReCaptchaService;
import uk.co.vhome.rmj.site.world.UserRegistrationFormObject;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolationException;

/**
 * Validates that all the details provided in the submitted registration form object are valid
 */
public class UserRegistrationValidator implements ConstraintValidator<UserRegistrationValid, UserRegistrationFormObject>
{

	private final static Logger LOGGER = LogManager.getLogger();

	private final ReCaptchaService recaptchaService;

	@Inject
	public UserRegistrationValidator(ReCaptchaService recaptchaService)
	{
		this.recaptchaService = recaptchaService;
	}

	@Override
	public void initialize(UserRegistrationValid userRegistrationValid)
	{

	}

	@Override
	public boolean isValid(UserRegistrationFormObject formObject, ConstraintValidatorContext constraintValidatorContext)
	{
		String reCaptchaResponse = formObject.getReCaptchaResponse();
		try
		{
			boolean responseValid = recaptchaService.isResponseValid(reCaptchaResponse, null);

			if ( !responseValid )
			{
				constraintValidatorContext.buildConstraintViolationWithTemplate("{validation.constraint.invalid.recaptchaResponse}")
						.addPropertyNode("reCaptchaResponse")
						.addConstraintViolation();
			}

			return responseValid;
		}
		catch (ConstraintViolationException e)
		{
			LOGGER.error("User registration failed validation", e.getMessage());
			return false;
		}

	}
}
