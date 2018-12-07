package uk.co.vhome.clubbed.apiobjects;

/**
 * Raised when a member or newly registered visitor has successfully claimed
 * their free token
 */
public class FreeTokenAcceptedEvent
{
	private final String emailAddress;

	public FreeTokenAcceptedEvent(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}
}
