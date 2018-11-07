package uk.co.vhome.clubbed.web.controllers.usermanagement;

import java.util.ArrayList;
import java.util.List;

public class UserManagementFormObject
{
	private List<UserManagementFormRow> userManagementFormRows = new ArrayList<>();

	public List<UserManagementFormRow> getUserManagementFormRows()
	{
		return userManagementFormRows;
	}

	public void setUserManagementFormRows(List<UserManagementFormRow> userManagementFormRows)
	{
		this.userManagementFormRows = userManagementFormRows;
	}

}
