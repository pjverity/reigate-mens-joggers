package uk.co.vhome.rmj.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import uk.co.vhome.rmj.security.filters.PostAuthenticationLoggingFilter;
import uk.co.vhome.rmj.security.filters.PreAuthenticationLoggingFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.util.EnumSet;

/**
 * Needed to bootstrap Spring Security's filter chain.
 *
 * Also sets up filters to add additional information to the requests before and after authentication. This is important
 * as it may be that some requests don't go through the security filters, so we can't just add filters after as they may never
 * execute. For example, adding fish tags should always been done regardless of whether security filters execute. Also
 * in the case a request does go through a security filter, execute the post authentication filters - ie, for adding
 * the principals username to the request/ThreadContext.
 */
@SuppressWarnings("unused")
public class BootstrapSecurity extends AbstractSecurityWebApplicationInitializer
{
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext)
	{
		LOGGER.info("Configuring pre-authentication filters...");

		FilterRegistration.Dynamic registration = servletContext.addFilter("preAuthenticationLoggingFilter", new PreAuthenticationLoggingFilter());
		registration.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
	}

	@Override
	protected void afterSpringSecurityFilterChain(ServletContext servletContext)
	{
		LOGGER.info("Configuring post-authentication filters...");

		FilterRegistration.Dynamic registration = servletContext.addFilter("postAuthenticationLoggingFilter", new PostAuthenticationLoggingFilter());
		registration.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
	}
}
