package uk.co.vhome.rmj.site.admin;

import uk.co.vhome.rmj.entities.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserManagementModel implements Serializable
{
	private List<User> users = new ArrayList<>();

	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(Iterable<User> users)
	{
		users.forEach(this.users::add);
	}
}
