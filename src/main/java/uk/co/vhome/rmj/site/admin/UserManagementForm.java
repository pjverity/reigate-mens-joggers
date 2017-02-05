package uk.co.vhome.rmj.site.admin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserManagementForm implements Serializable
{
	private Map<String, MutableUser> userSettings = new HashMap<>();

	public Map<String, MutableUser> getUserSettings()
	{
		return userSettings;
	}

	public void addUserSettings(String userId, MutableUser user)
	{
		userSettings.put(userId, user);
	}

}
