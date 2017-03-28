package uk.co.vhome.rmj.site.admin;

import uk.co.vhome.rmj.entities.UserDetailsEntity;

public class UserManagementFormRow
{
	private Long id;

	private boolean enabled;

	public UserManagementFormRow()
	{
	}

	public UserManagementFormRow(UserDetailsEntity userDetailsEntity)
	{
		this.id = userDetailsEntity.getId();
		this.enabled = userDetailsEntity.isEnabled();
	}

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
}
