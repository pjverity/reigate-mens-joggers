package uk.co.vhome.rmj.site.world;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import uk.co.vhome.rmj.site.form.validation.UserRegistrationValid;

import javax.validation.constraints.Pattern;

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

	@Override
	public String toString()
	{
		return "UserRegistrationFormObject{" +
				"emailAddress='" + emailAddress + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				'}';
	}
}
