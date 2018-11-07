package uk.co.vhome.clubbed.test;

import uk.co.vhome.clubbed.entities.UserDetailsEntity;
import uk.co.vhome.clubbed.entities.UserEntity;

public class UserConfigurations
{
	private static final Long ENABLED_USER_ID = 0L; //"test.user.enabled@home.co.uk";

	private static final Long DISABLED_USER_ID = 1L; //"test.user.disabled@home.co.uk";

	private static final Long ADMIN_USER_ID = 2L; //"admin.user@home.co.uk";

	private static final Long ORGANISER_USER_ID = 3L; //"organiser.user@home.co.uk";

	public static final UserEntity ENABLED_USER = new UserEntity(ENABLED_USER_ID, "test.user.enabled@home.co.uk", true);

	public static final UserEntity DISABLED_USER = new UserEntity(DISABLED_USER_ID, "test.user.disabled@home.co.uk", false);

	public static final UserEntity ADMIN_USER = new UserEntity(ADMIN_USER_ID, "admin.user@home.co.uk", true);

	public static final UserEntity ORGANISER_USER = new UserEntity(ORGANISER_USER_ID, "organiser.user@home.co.uk", true);

	private static final UserDetailsEntity ENABLED_USER_DETAILS = new UserDetailsEntity(ENABLED_USER, "Test", "User (Enabled)");
	private static final UserDetailsEntity DISABLED_USER_DETAILS = new UserDetailsEntity(DISABLED_USER, "Test", "User (Disabled)");
	private static final UserDetailsEntity ADMIN_USER_DETAILS = new UserDetailsEntity(ADMIN_USER, "Admin", "User");
	private static final UserDetailsEntity ORGANISER_USER_DETAILS = new UserDetailsEntity(ORGANISER_USER, "Organiser", "User");

	static {
		ENABLED_USER.setUserDetailsEntity(ENABLED_USER_DETAILS);
		DISABLED_USER.setUserDetailsEntity(DISABLED_USER_DETAILS);
		ADMIN_USER.setUserDetailsEntity(ADMIN_USER_DETAILS);
		ORGANISER_USER.setUserDetailsEntity(ORGANISER_USER_DETAILS);
	}

}
