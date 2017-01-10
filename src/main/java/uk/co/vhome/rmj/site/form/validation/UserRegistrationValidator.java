package uk.co.vhome.rmj.site.form.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.vhome.rmj.services.ReCaptchaService;
import uk.co.vhome.rmj.services.UserRegistrationService;
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

	private final UserRegistrationService userRegistrationService;

	@Inject
	public UserRegistrationValidator(ReCaptchaService recaptchaService, UserRegistrationService userRegistrationService)
	{
		this.recaptchaService = recaptchaService;
		this.userRegistrationService = userRegistrationService;
	}

	@Override
	public void initialize(UserRegistrationValid userRegistrationValid)
	{

	}

	@Override
	public boolean isValid(UserRegistrationFormObject formObject, ConstraintValidatorContext constraintValidatorContext)
	{
		boolean registrationValid = true;

		if ( userRegistrationService.isEmailAddressInUse(formObject.getEmailAddress()) )
		{
			constraintValidatorContext.buildConstraintViolationWithTemplate("{validation.constraint.UserRegistrationValid.emailAddress}")
					.addPropertyNode("emailAddress")
					.addConstraintViolation();

			registrationValid = false;
		}

		String reCaptchaResponse = formObject.getReCaptchaResponse();
		try
		{
			boolean responseValid = recaptchaService.isResponseValid(reCaptchaResponse, null);

			if ( !responseValid )
			{
				constraintValidatorContext.buildConstraintViolationWithTemplate("{validation.constraint.UserRegistrationValid.recaptchaResponse}")
						.addPropertyNode("reCaptchaResponse")
						.addConstraintViolation();

				registrationValid = false;
			}

			return registrationValid;
		}
		catch (ConstraintViolationException e)
		{
			LOGGER.error("User registration failed validation", e);
			return false;
		}

	}
}
