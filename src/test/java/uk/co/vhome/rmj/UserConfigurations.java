package uk.co.vhome.rmj;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class UserConfigurations
{
	public static final String ENABLED_USER_ID = "test.user.enabled@home.co.uk";

	public static final String DISABLED_USER_ID = "test.user.disabled@home.co.uk";

	public static final User ENABLED_USER = new User(ENABLED_USER_ID,
	                                                 "",
	                                                 true,
	                                                 false,
	                                                 false,
	                                                 false,
	                                                 AuthorityUtils.NO_AUTHORITIES);

	public static final User DISABLED_USER = new User(DISABLED_USER_ID,
	                                                  "",
	                                                  false,
	                                                  false,
	                                                  false,
	                                                  false,
	                                                  AuthorityUtils.NO_AUTHORITIES);
}
