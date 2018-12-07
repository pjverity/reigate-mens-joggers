package uk.co.vhome.clubbed.web.site.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.UserDetailsManager;
import uk.co.vhome.clubbed.web.site.member.PasswordChangeFormObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Validates the password changes meet all the necessary criteria
 */
public class PasswordChangeValidator implements ConstraintValidator<PasswordChangeValid, PasswordChangeFormObject>
{
	private final UserDetailsManager userDetailsManager;

	@Autowired
	public PasswordChangeValidator(UserDetailsManager userDetailsManager)
	{
		this.userDetailsManager = userDetailsManager;
	}

	@Override
	public void initialize(PasswordChangeValid valid) { }

	@Override
	public boolean isValid(PasswordChangeFormObject passwordChangeFormObject, ConstraintValidatorContext constraintValidatorContext)
	{
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
