package uk.co.vhome.rmj.notifications;

import uk.co.vhome.rmj.entities.UserDetailsEntity;

import java.util.Set;

public class NewUserNotification
{
	private final UserDetailsEntity userDetails;

	private final Set<UserDetailsEntity> enabledAdminDetails;

	public NewUserNotification(UserDetailsEntity userDetails, Set<UserDetailsEntity> enabledAdminDetails)
	{
		this.userDetails = userDetails;
		this.enabledAdminDetails = enabledAdminDetails;
	}

	public UserDetailsEntity getUserDetails()
	{
		return userDetails;
	}

	public Set<UserDetailsEntity> getEnabledAdminDetails()
	{
		return enabledAdminDetails;
	}
}
