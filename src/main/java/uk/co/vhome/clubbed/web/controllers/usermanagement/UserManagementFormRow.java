package uk.co.vhome.clubbed.web.controllers.usermanagement;

import uk.co.vhome.clubbed.entities.UserDetailsEntity;

public class UserManagementFormRow
{
	private UserDetailsEntity userEntity;

	public UserManagementFormRow()
	{
	}

	public UserManagementFormRow(UserDetailsEntity userEntity)
	{
		this.userEntity = userEntity;
	}

	public UserDetailsEntity getUserEntity()
	{
		return userEntity;
	}

	public void setUserEntity(UserDetailsEntity userEntity)
	{
		this.userEntity = userEntity;
	}

	/*
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	*/
}
