package uk.co.vhome.rmj.site.form.validation;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.UserDetailsManager;
import uk.co.vhome.rmj.site.member.PasswordChangeFormObject;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Validates the password changes meet all the neccessary criteria
 */
public class PasswordChangeValidator implements ConstraintValidator<PasswordChangeValid, PasswordChangeFormObject>
{
	private final UserDetailsManager userDetailsManager;

	private User principal;

	@Inject
	public PasswordChangeValidator(UserDetailsManager userDetailsManager)
	{
		this.userDetailsManager = userDetailsManager;
	}

	@Override
	public void initialize(PasswordChangeValid valid)
	{
		principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Override
	public boolean isValid(PasswordChangeFormObject passwordChangeFormObject, ConstraintValidatorContext constraintValidatorContext)
	{
		UserDetails userDetails = userDetailsManager.loadUserByUsername(principal.getUsername());

		if ( !Objects.equals(passwordChangeFormObject.getNewPassword(), passwordChangeFormObject.getConfirmedNewPassword()) )
		{
			constraintValidatorContext.buildConstraintViolationWithTemplate("{validation.constraint.PasswordChangeValid.confirmPassword}")
					.addPropertyNode("newPassword")
					.addConstraintViolation();
			constraintValidatorContext.buildConstraintViolationWithTemplate("{validation.constraint.PasswordChangeValid.confirmPassword}")
					.addPropertyNode("confirmedNewPassword")
					.addConstraintViolation();

			return false;
		}

		if ( !BCrypt.checkpw(passwordChangeFormObject.getOldPassword(), userDetails.getPassword()))
		{
			constraintValidatorContext.buildConstraintViolationWithTemplate("{validation.constraint.PasswordChangeValid.notOldPassword}")
					.addPropertyNode("oldPassword")
					.addConstraintViolation();

			return false;
		}

		return true;
	}
}
