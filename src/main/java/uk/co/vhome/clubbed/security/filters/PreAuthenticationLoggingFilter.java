package uk.co.vhome.clubbed.security.filters;

import org.apache.logging.log4j.CloseableThreadContext;
import uk.co.vhome.clubbed.security.BootstrapSecurity;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * A servlet filter that accepts all types of dispatcher requests and adds additional diagnostic
 * information to the request for logging. This filter executes before Springs authentication
 * filter. Filter order defined in {@link BootstrapSecurity}
 */
public class PreAuthenticationLoggingFilter implements Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException { }

	@Override
	public void destroy() { }

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
	{
		try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.put("id", UUID.randomUUID().toString()) )
		{
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

}
