package uk.co.vhome.rmj.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * Helper class to temporarily replace the user in the current security context before executing
 * a given task. Restores the original context when the task completes. Required when calling methods
 * that require a given level of authentication.
 *
 * Todo - Look in to using role hierarchies
 */
public class AuthenticatedUser
{

	private static final Authentication SYSTEM_USER = new UsernamePasswordAuthenticationToken("system",
	                                                                                         "",
	                                                                                         AuthorityUtils.createAuthorityList(Role.SYSTEM,
	                                                                                                                            Role.ADMIN,
	                                                                                                                            Role.ORGANISER,
	                                                                                                                            Role.MEMBER,
	                                                                                                                            Role.ANONYMOUS));


	public static <V> V callAsSystemUser(Callable<V> task) throws Exception
	{
		return callAs(SYSTEM_USER, task);
	}

	public static void runWithSystemUser(Runnable task)
	{
		try
		{
			callAs(SYSTEM_USER, Executors.callable(task));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private static <V> V callAs(Authentication authentication, Callable<V> task) throws Exception
	{
		SecurityContext origCtx = SecurityContextHolder.getContext();

		try
		{
			SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return task.call();
		}
		finally
		{
			SecurityContextHolder.setContext(origCtx);
		}
	}
}
