package uk.co.vhome.rmj;

import uk.co.vhome.rmj.entities.UserDetailsEntity;

public class UserConfigurations
{
	public static final Long ENABLED_USER_ID = 0L; //"test.user.enabled@home.co.uk";

	public static final Long DISABLED_USER_ID = 1L; //"test.user.disabled@home.co.uk";

	public static final UserDetailsEntity ENABLED_USER = new UserDetailsEntity(ENABLED_USER_ID, "test.user.enabled@home.co.uk", true);

	public static final UserDetailsEntity DISABLED_USER = new UserDetailsEntity(DISABLED_USER_ID, "test.user.disabled@home.co.uk", false);
}
