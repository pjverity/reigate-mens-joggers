package uk.co.vhome.rmj.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Helper class to put an authenticated user in the current security context before executing
 * the given task. Clears the context when the task completes. Required when calling methods
 * that require a given level of authentication, or to 'Run As' a particular user.
 */
public class AuthenticatedUser
{

	private static final Authentication SYSTEM_USER = new UsernamePasswordAuthenticationToken("system",
	                                                                                         "",
	                                                                                         AuthorityUtils.createAuthorityList(Role.SYSTEM,
	                                                                                                                            Role.ADMIN,
	                                                                                                                            Role.ORGANISER,
	                                                                                                                            Role.MEMBER,
	                                                                                                                            Role.ANON));

	private static final Authentication ANON_USER = new UsernamePasswordAuthenticationToken("anon",
	                                                                                       "",
	                                                                                       AuthorityUtils.createAuthorityList(Role.ANON));

	public static void runWithSystemUser(Runnable task)
	{
		runWith(SYSTEM_USER, task);
	}

	public static void runWithAnonUser(Runnable task)
	{
		runWith(ANON_USER, task);
	}

	private static void runWith(Authentication authentication, Runnable task)
	{
		try
		{
			SecurityContextHolder.getContext().setAuthentication(authentication);
			task.run();
		}
		finally
		{
			SecurityContextHolder.clearContext();
		}
	}
}
