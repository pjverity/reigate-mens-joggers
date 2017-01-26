package uk.co.vhome.rmj.site.form.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.provisioning.UserDetailsManager;
import uk.co.vhome.rmj.services.ReCaptchaService;
import uk.co.vhome.rmj.site.world.UserRegistrationFormObject;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * Validates that all the details provided in the submitted registration form object are valid
 */
public class UserRegistrationValidator implements ConstraintValidator<UserRegistrationValid, UserRegistrationFormObject>
{

	private final static Logger LOGGER = LogManager.getLogger();

	private final ReCaptchaService recaptchaService;

	private final UserDetailsManager userDetailsManager;

	@Inject
	public UserRegistrationValidator(ReCaptchaService recaptchaService, UserDetailsManager userDetailsManager)
	{
		this.recaptchaService = recaptchaService;
		this.userDetailsManager = userDetailsManager;
	}

	@Override
	public void initialize(UserRegistrationValid userRegistrationValid)
	{

	}

	@Override
	public boolean isValid(UserRegistrationFormObject formObject, ConstraintValidatorContext constraintValidatorContext)
	{
		boolean registrationValid = true;

		if ( userDetailsManager.userExists(formObject.getEmailAddress()) )
		{
			constraintValidatorContext.buildConstraintViolationWithTemplate("{validation.constraint.UserRegistrationValid.emailAddress}")
					.addPropertyNode("emailAddress")
					.addConstraintViolation();

			registrationValid = false;
		}

		if ( !Objects.equals(formObject.getEmailAddress(), formObject.getConfirmEmailAddress()) )
		{
			constraintValidatorContext.buildConstraintViolationWithTemplate("{validation.constraint.UserRegistrationValid.confirmEmailAddress}")
					.addPropertyNode("confirmEmailAddress")
					.addConstraintViolation();

			registrationValid = false;
		}

		if ( !Objects.equals(formObject.getPassword(), formObject.getReenteredPassword()) )
		{
			constraintValidatorContext.buildConstraintViolationWithTemplate("{validation.constraint.UserRegistrationValid.confirmPassword}")
					.addPropertyNode("reenteredPassword")
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
