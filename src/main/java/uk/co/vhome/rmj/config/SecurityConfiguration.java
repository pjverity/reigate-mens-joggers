package uk.co.vhome.rmj.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import uk.co.vhome.rmj.entities.UserDetail;
import uk.co.vhome.rmj.repositories.UserDetailsRepository;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collections;

import static uk.co.vhome.rmj.config.BootstrapFramework.ADDITIONAL_RESOURCE_PATHS;

/*
 * Configure web security for the site
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, order = 0)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final DataSource dataSource;

	private JdbcUserDetailsManager userDetailsManager;

	@Inject
	private UserDetailsRepository userDetailsRepository;

	@Inject
	public SecurityConfiguration(DataSource dataSource)
	{
		this.dataSource = dataSource;
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

		// If the database is empty, set up a default admin account
		initialiseAdminUser((UserDetailsManager)auth.getDefaultUserDetailsService());
	}

	@Bean
	public JdbcUserDetailsManager userDetailsManager(AuthenticationManagerBuilder auth) throws Exception
	{
		configureAuth(auth);
		return userDetailsManager;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		LOGGER.info("Configuring HTTP security...");

		http.authorizeRequests()
					.antMatchers("/admin/**").hasAuthority("ADMIN")
					.antMatchers("/member/**").hasAnyAuthority("MEMBER", "ADMIN")
					.antMatchers("/**").permitAll()
				.and()
					.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/")
					.failureUrl("/?error")
					.permitAll()
					.successHandler(successHandler())
				.and()
					.logout()
					.logoutSuccessUrl("/")
					.deleteCookies("JSESSIONID");
	}


	@Override
	public void configure(WebSecurity web) throws Exception
	{
		LOGGER.info("Configuring Web security...");

		// Bypass the security filters for efficiency
		web.ignoring().antMatchers(ADDITIONAL_RESOURCE_PATHS);
	}

	private AuthenticationSuccessHandler successHandler()
	{
		return new SavedRequestAwareAuthenticationSuccessHandler()
		{
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException
			{
				super.onAuthenticationSuccess(request, response, authentication);

				UserDetail userDetail = userDetailsRepository.findOne(authentication.getName());
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute(ServletContextConfiguration.USER_FIRST_NAME_SESSION_ATTRIBUTE, userDetail.getFirstName());
				httpSession.setAttribute(ServletContextConfiguration.USER_LAST_NAME_SESSION_ATTRIBUTE, userDetail.getLastName());
			}
		};
	}

	private void initialiseAdminUser(UserDetailsManager userDetailsManager)
	{
		if ( !userDetailsManager.userExists("admin") )
		{
			LOGGER.warn("Generating default admin user. Log in as admin user and change the default password");

			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ADMIN");
			userDetailsManager.createUser(new User("admin", BCrypt.hashpw("test", BCrypt.gensalt()), Collections.singleton(authority)));
		}

		if ( !userDetailsManager.userExists("member") )
		{
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("MEMBER");
			userDetailsManager.createUser(new User("member", BCrypt.hashpw("test", BCrypt.gensalt()), Collections.singleton(authority)));
		}
	}
}
