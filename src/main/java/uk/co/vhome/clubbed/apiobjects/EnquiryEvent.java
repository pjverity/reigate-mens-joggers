package uk.co.vhome.clubbed.apiobjects;

public abstract class EnquiryEvent
{
	private final String emailAddress;

	public EnquiryEvent(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}
}
