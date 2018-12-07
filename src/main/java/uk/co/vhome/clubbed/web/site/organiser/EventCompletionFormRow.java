package uk.co.vhome.clubbed.web.site.organiser;

import uk.co.vhome.clubbed.entities.UserDetailsEntity;

/**
 * Represents a row in the event registration table for registering the presence of a member
 */
public class EventCompletionFormRow
{
	private UserDetailsEntity userDetailsEntity;

	private boolean present;

	public EventCompletionFormRow(UserDetailsEntity userDetailsEntity)
	{
		this.userDetailsEntity = userDetailsEntity;
	}

	public UserDetailsEntity getUserDetailsEntity()
	{
		return userDetailsEntity;
	}

	public void setUserDetailsEntity(UserDetailsEntity userDetailsEntity)
	{
		this.userDetailsEntity = userDetailsEntity;
	}

	public boolean isPresent()
	{
		return present;
	}

	public void setPresent(boolean present)
	{
		this.present = present;
	}
}
