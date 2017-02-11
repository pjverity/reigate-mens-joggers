package uk.co.vhome.rmj.site.admin;

import uk.co.vhome.rmj.services.UserAccountDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class UserManagementForm
{
	private List<UserAccountDetails> userAccountDetails = new LinkedList<>();

	public Collection<UserAccountDetails> getUserAccountDetails()
	{
		return userAccountDetails;
	}

	public void setUserAccountDetails(List<UserAccountDetails> userAccountDetails)
	{
		this.userAccountDetails = userAccountDetails;
	}

}
