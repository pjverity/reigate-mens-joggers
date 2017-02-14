package uk.co.vhome.rmj.services;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Aggregated user information, combining core user account information with supplementary user information.
 * This is used by user account management screens.
 */
public class UserAccountDetails
{
	private String emailAddress;

	private final String firstName;

	private final String lastName;

	private String group;

	private final LocalDateTime lastLogin;

	private final boolean activeSession;

	private boolean enabled;

	/*
	 * Used as a form object, so needs a no-arg ctor
	 */
	public UserAccountDetails()
	{
		firstName = "";
		lastName = "";
		lastLogin = LocalDateTime.now();
		activeSession = false;
	}

	UserAccountDetails(String emailAddress, String firstName, String lastName, String group, boolean enabled, LocalDateTime lastLogin, boolean activeSession)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.group = group;
		this.enabled = enabled;
		this.lastLogin = lastLogin;
		this.activeSession = activeSession;
	}

	public String getFullName()
	{
		return String.join(" ", firstName, lastName);
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	/*
	 * Only mutable to allow the form to set the email address when posted back to the controller
	 */
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public String getLastLogin()
	{
		return lastLogin.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
	}

	public boolean isActiveSession()
	{
		return activeSession;
	}
}
