package uk.co.vhome.rmj;

import uk.co.vhome.rmj.entities.UserDetailsEntity;

public class UserConfigurations
{
	public static final String ENABLED_USER_ID = "test.user.enabled@home.co.uk";

	public static final String DISABLED_USER_ID = "test.user.disabled@home.co.uk";

	public static final UserDetailsEntity ENABLED_USER = new UserDetailsEntity(ENABLED_USER_ID, true);

	public static final UserDetailsEntity DISABLED_USER = new UserDetailsEntity(DISABLED_USER_ID, false);
}
