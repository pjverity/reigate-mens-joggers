package uk.co.vhome.rmj;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class UserConfigurations
{
	public static final String USER_ID = "user@home.co.uk";

	public static final User DISABLED_USER = new User(USER_ID,
	                                                  "",
	                                                  false,
	                                                  false,
	                                                  false,
	                                                  false,
	                                                  AuthorityUtils.NO_AUTHORITIES);

	public static final User ENABLED_USER = new User(USER_ID,
	                                                  "",
	                                                  true,
	                                                  false,
	                                                  false,
	                                                  false,
	                                                  AuthorityUtils.NO_AUTHORITIES);
}
