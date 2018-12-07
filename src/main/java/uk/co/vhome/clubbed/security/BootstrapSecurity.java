package uk.co.vhome.clubbed.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import uk.co.vhome.clubbed.security.filters.PostAuthenticationLoggingFilter;
import uk.co.vhome.clubbed.security.filters.PreAuthenticationLoggingFilter;

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
@Order(Ordered.HIGHEST_PRECEDENCE)
//@Configuration
@EnableWebSecurity
//@Import(SecurityConfiguration.class)
public class BootstrapSecurity extends AbstractSecurityWebApplicationInitializer implements ServletContextInitializer
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapSecurity.class);

	/*
	 * Enable session events so we can use the SessionRegistry to get information about
	 * who is logged in, or how many recent connections there have been.
	 */
	@Override
	protected boolean enableHttpSessionEventPublisher()
	{
		return true;
	}

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

	/*
	 * Removed for now as AuthenticatedUser class is used to elevate privileges. RunAs.NEW_USER is not used either
	 *
	 * Configure the Global Method Security to use 'RunAs' authentication, so 'system' level calls
	 * can be made from user initiated calls. For example, a new user registers, but requires system
	 * level access for the service to call methods on the e-mail service.
	 */
	/*
	@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true)
	public class MethodSecurity extends GlobalMethodSecurityConfiguration
	{
		@Override
		protected RunAsManager runAsManager()
		{
			RunAsManagerImpl runAsManager = new RunAsManagerImpl();
			runAsManager.setKey("my_key");
			return runAsManager;
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception
		{
			auth.authenticationProvider(runAsAuthenticationProvider());
		}

		@Bean
		AuthenticationProvider runAsAuthenticationProvider()
		{
			RunAsImplAuthenticationProvider authenticationProvider = new RunAsImplAuthenticationProvider();
			authenticationProvider.setKey("my_key");
			return authenticationProvider;
		}
	}
	*/
}
