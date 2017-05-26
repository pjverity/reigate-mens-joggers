package uk.co.vhome.rmj;

import uk.co.vhome.rmj.entities.UserDetailsEntity;

public class UserConfigurations
{
	public static final Long ENABLED_USER_ID = 0L; //"test.user.enabled@home.co.uk";

	public static final Long DISABLED_USER_ID = 1L; //"test.user.disabled@home.co.uk";

	public static final Long ADMIN_USER_ID = 2L; //"admin.user@home.co.uk";

	public static final Long ORGANISER_USER_ID = 3L; //"organiser.user@home.co.uk";

	public static final UserDetailsEntity ENABLED_USER = new UserDetailsEntity(ENABLED_USER_ID, "test.user.enabled@home.co.uk", true);

	public static final UserDetailsEntity DISABLED_USER = new UserDetailsEntity(DISABLED_USER_ID, "test.user.disabled@home.co.uk", false);

	public static final UserDetailsEntity ADMIN_USER = new UserDetailsEntity(ADMIN_USER_ID, "admin.user@home.co.uk", true);

	public static final UserDetailsEntity ORGANISER_USER = new UserDetailsEntity(ORGANISER_USER_ID, "organiser.user@home.co.uk", true);
}
