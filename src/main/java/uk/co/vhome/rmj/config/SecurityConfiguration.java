package uk.co.vhome.rmj.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.intercept.RunAsManagerImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import uk.co.vhome.rmj.entities.SupplementalUserDetails;
import uk.co.vhome.rmj.repositories.SupplementalUserDetailsRepository;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;

import static uk.co.vhome.rmj.config.BootstrapFramework.ADDITIONAL_RESOURCE_PATHS;

/*
 * Configure web security for the site
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final DataSource dataSource;

	private final SupplementalUserDetailsRepository supplementalUserDetailsRepository;

	private JdbcUserDetailsManager userDetailsManager;

	@Inject
	public SecurityConfiguration(DataSource dataSource,
	                             SupplementalUserDetailsRepository supplementalUserDetailsRepository)
	{
		this.dataSource = dataSource;
		this.supplementalUserDetailsRepository = supplementalUserDetailsRepository;
	}

	private void configureAuth(AuthenticationManagerBuilder auth) throws Exception
	{
		LOGGER.info("Configuring authentication...");

		// Store users in a database using a JDBC datasource to connect to it. So that passwords are not
		// stored in the database in plain text, they are hashed using the given password encoder
		userDetailsManager = auth.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery("select username, password, enabled from users where lower(username) = lower(?)")
				.passwordEncoder(new BCryptPasswordEncoder()).getUserDetailsService();

		userDetailsManager.setEnableAuthorities(false);
		userDetailsManager.setEnableGroups(true);
	}

	@Bean
	public JdbcUserDetailsManager userDetailsManager(AuthenticationManagerBuilder auth,
	                                                 ApplicationEventPublisher applicationEventPublisher) throws Exception
	{
		configureAuth(auth);

		auth.authenticationEventPublisher(new DefaultAuthenticationEventPublisher(applicationEventPublisher));

		return userDetailsManager;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		LOGGER.info("Configuring HTTP security...");

		http.authorizeRequests()
					.antMatchers("/admin/**").hasRole("ADMIN")
					.antMatchers("/member/**").hasAnyRole("MEMBER", "ADMIN")
					.antMatchers("/**").permitAll()
				.and()
					.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/")
					.failureUrl("/?error")
					.permitAll()
					.successHandler(authenticationSuccessHandler())
				.and()
					.logout()
					.logoutSuccessUrl("/")
					.deleteCookies("JSESSIONID")
				.and()
					.sessionManagement().invalidSessionUrl("/");
	}


	@Override
	public void configure(WebSecurity web) throws Exception
	{
		LOGGER.info("Configuring Web security...");

		// Bypass the security filters for efficiency
		web.ignoring().antMatchers(ADDITIONAL_RESOURCE_PATHS);
	}

	/*
	 * This seems to be the best place to add session attributes after a successful login. However, it's not
	 * a suitable place to inject services that have method security annotations as it appears to cause a
	 * circular bean reference, so use the Authentication event listener instead to interact with any
	 * injected secured services.
	 */
	private AuthenticationSuccessHandler authenticationSuccessHandler()
	{
		return new SavedRequestAwareAuthenticationSuccessHandler()
		{
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException
			{
				super.onAuthenticationSuccess(request, response, authentication);

				SupplementalUserDetails details = supplementalUserDetailsRepository.findByEmailAddress(authentication.getName());
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute(ServletContextConfiguration.USER_FIRST_NAME_SESSION_ATTRIBUTE, details.getFirstName());
				httpSession.setAttribute(ServletContextConfiguration.USER_LAST_NAME_SESSION_ATTRIBUTE, details.getLastName());
			}
		};
	}

	/**
	 * Configure the Global Method Security to use 'RunAs' authentication, so 'system' level calls
	 * can be made from user initiated calls. For example, a new user registers, but requires system
	 * level access for the service to call methods on the e-mail service.
	 *
	 * Spring will component scan this as a bean. The alternative is to put it in a top level class
	 * an @Import it from the outer class.
	 */
	@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true, order = 0)
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
}
