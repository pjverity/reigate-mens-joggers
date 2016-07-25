package uk.co.vhome.rmj.config.filters;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import java.io.IOException;

/**
 * Servlet filter that is added after Springs security filters so that we can place the authenticated users
 * username in the thread context for use in log messages.
 *
 * Filter order defined in {@link uk.co.vhome.rmj.config.BootstrapSecurity}
 */
public class PostAuthenticationLoggingFilter implements Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException { }

	@Override
	public void destroy() { }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null )
		{
			ThreadContext.put("username", authentication.getName());
		}

		chain.doFilter(request, response);

	}
}
