package uk.co.vhome.clubbed.web.controllers.userregistration;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.StringJoiner;

/**
 * The object that defines the user sign up form, that is validated to ensure
 * all inputs are valid for sign-up
 */
@UserRegistrationValid
public class UserRegistrationFormObject
{

	@NotBlank(message = "{validation.constraint.Blank.recaptchaResponse}")
	private String reCaptchaResponse;

	@NotBlank(message = "{validation.constraint.Blank.emailAddress}")
	@Email
	private String emailAddress;

	private String confirmEmailAddress;

	@Size(min = 8, message = "{validation.constraint.Min.password}")
	private String password;

	private String reenteredPassword;

	@NotBlank(message = "{validation.constraint.Blank.firstName}")
	@Pattern(regexp = "[a-zA-Z- ]+", message = "{validation.constraint.Pattern.invalidName}")
	private String firstName;

	@NotBlank(message = "{validation.constraint.Blank.lastName}")
	@Pattern(regexp = "[a-zA-Z- ]+", message = "{validation.constraint.Pattern.invalidName}")
	private String lastName;

	public String getReCaptchaResponse()
	{
		return reCaptchaResponse;
	}

	public void setReCaptchaResponse(String reCaptchaResponse)
	{
		this.reCaptchaResponse = reCaptchaResponse;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getConfirmEmailAddress()
	{
		return confirmEmailAddress;
	}

	public void setConfirmEmailAddress(String confirmEmailAddress)
	{
		this.confirmEmailAddress = confirmEmailAddress;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getReenteredPassword()
	{
		return reenteredPassword;
	}

	public void setReenteredPassword(String reenteredPassword)
	{
		this.reenteredPassword = reenteredPassword;
	}


	@Override
	public String toString()
	{
		return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
				       .add("firstName = " + firstName)
				       .add("lastName = " + lastName)
				       .add("emailAddress = " + emailAddress)
				       .add("confirmEmailAddress = " + confirmEmailAddress)
				       .toString();
	}
}
