package uk.co.vhome.rmj.site.filters;

import org.apache.logging.log4j.CloseableThreadContext;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

/**
 * A servlet filter that accepts all types of dispatcher requests and adds additional diagnostic
 * information to the request for logging.
 */
@WebFilter(filterName = "loggingFilter",
		urlPatterns = {"/*"},
		dispatcherTypes = {DispatcherType.ERROR, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.REQUEST, DispatcherType.ASYNC}
		)
public class LoggingFilter implements Filter
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
