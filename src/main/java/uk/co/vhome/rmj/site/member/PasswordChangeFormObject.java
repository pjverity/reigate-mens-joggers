package uk.co.vhome.rmj.site.member;

import org.hibernate.validator.constraints.NotBlank;
import uk.co.vhome.rmj.site.form.validation.PasswordChangeValid;

/**
 * A form object that encapsulates password changes to be validated
 */
@PasswordChangeValid
public class PasswordChangeFormObject
{
	private String oldPassword;

	private boolean passwordChanged;

	@NotBlank(message = "{validation.constraint.NotBlank.newPassword}")
	private String newPassword;

	private String confirmedNewPassword;

	public String getOldPassword()
	{
		return oldPassword;
	}

	public void setOldPassword(String oldPassword)
	{
		this.oldPassword = oldPassword;
	}

	public String getNewPassword()
	{
		return newPassword;
	}

	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}

	public String getConfirmedNewPassword()
	{
		return confirmedNewPassword;
	}

	public void setConfirmedNewPassword(String confirmedNewPassword)
	{
		this.confirmedNewPassword = confirmedNewPassword;
	}

	public boolean isPasswordChanged()
	{
		return passwordChanged;
	}

	public void setPasswordChanged(boolean passwordChanged)
	{
		this.passwordChanged = passwordChanged;
	}
}
