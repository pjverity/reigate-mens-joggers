package uk.co.vhome.rmj.site.admin;

import uk.co.vhome.rmj.entities.UserPrincipal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserManagementModel implements Serializable
{
	private List<UserPrincipal> users = new ArrayList<>();

	public List<UserPrincipal> getUsers()
	{
		return users;
	}

	public void setUsers(Iterable<UserPrincipal> users)
	{
		users.forEach(this.users::add);
	}
}
