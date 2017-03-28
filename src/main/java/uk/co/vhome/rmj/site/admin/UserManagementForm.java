package uk.co.vhome.rmj.site.admin;

import java.util.ArrayList;
import java.util.List;

public class UserManagementForm
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
