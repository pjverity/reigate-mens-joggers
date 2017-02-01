package uk.co.vhome.rmj.site.admin;

import uk.co.vhome.rmj.entities.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserManagementForm implements Serializable
{
	private List<User> users = new ArrayList<>();

	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}
}
