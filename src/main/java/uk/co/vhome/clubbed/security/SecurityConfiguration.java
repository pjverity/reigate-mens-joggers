package uk.co.vhome.clubbed.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Configure web security for the site
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static class SimpleUser
	{
		private final int userId;
		private final String firstName;
		private final String lastName;

		SimpleUser(int userId, String firstName, String lastName)
		{
			this.userId = userId;
			this.firstName = firstName;
			this.lastName = lastName;
		}
	}

	private static final String SESSION_ATTRIBUTES_SQL = "SELECT id, first_name, last_name " +
			                                                     "FROM users, user_details " +
			                                                     "WHERE username = ? AND id = users_id";

	/*
	 * /images are not packaged with the war, but are served by Tomcat from a directory on the filesystem
	 * This is configured in a /Context/Resources/PreResource element in context.xml.
	 *
	 * The element accepts a filesystem path and a url that will cause the container to load
	 * the resource from the filesystem rather than from the contents of the web app.
	 */
	private static final String[] UNPROTECTED_RESOURCE_MATCHERS = {"/css/**",
	                                                               "/galleria/**",
	                                                               "/images/**",
	                                                               "/js/**",
	                                                               "/sitemap.xml"};

	public static final String USER_ID_SESSION_ATTRIBUTE = "userId";

	public static final String USER_FIRST_NAME_SESSION_ATTRIBUTE = "userFirstName";

	public static final String USER_LAST_NAME_SESSION_ATTRIBUTE = "userLastName";

	private final DataSource dataSource;

	private JdbcUserDetailsManager userDetailsManager;

	@Autowired
	public SecurityConfiguration(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
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

	@Bean(name = "userDetailsManager")
	@Override
	public JdbcUserDetailsManager userDetailsServiceBean()
	{
		return userDetailsManager;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		LOGGER.info("Configuring HTTP security...");

		// TODO: Roles are not tied to those defined in 'clubbed-user-management'
		http.authorizeRequests()
					.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
					.antMatchers("/organiser/**").hasAuthority("ROLE_ORGANISER")
					.antMatchers("/member/**").hasAuthority("ROLE_MEMBER")
					.antMatchers("/**").permitAll()
				.and()
					.formLogin()
					.loginPage("/")
					.loginProcessingUrl("/login")
					//.defaultSuccessUrl("/member/home") -> Overwritten by authenticationSuccessHandler()
					.failureUrl("/?error")
					.permitAll()
					.successHandler(authenticationSuccessHandler())
				.and()
					.logout()
					.logoutSuccessUrl("/")
					.deleteCookies("JSESSIONID")
				.and()
					.sessionManagement()
					.invalidSessionUrl("/")
					.maximumSessions(1)
					.sessionRegistry(sessionRegistry());
	}

	/*
	 * Expose this to other services to use
	 */
	@Bean
	SessionRegistry sessionRegistry()
	{
		return new SessionRegistryImpl();
	}

	@Override
	public void configure(WebSecurity web)
	{
		LOGGER.info("Configuring Web security...");

		// Bypass the security filters for efficiency
		web.ignoring().antMatchers(UNPROTECTED_RESOURCE_MATCHERS);
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
				setDefaultTargetUrl("/member/home");

				SimpleUser simpleUser = userDetailsManager.getJdbcTemplate().queryForObject(SESSION_ATTRIBUTES_SQL, new String[]{authentication.getName()}, new RowMapper<>()
				{
					@Override
					public SimpleUser mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						return new SimpleUser(rs.getInt(1),
						                      rs.getString(2),
						                      rs.getString(3));
					}
				});

				if ( simpleUser == null )
				{
					throw new IllegalStateException(authentication.getName() + " was authenticated but unable to locate user details");
				}

				HttpSession httpSession = request.getSession();
				httpSession.setAttribute(USER_ID_SESSION_ATTRIBUTE, simpleUser.userId);
				httpSession.setAttribute(USER_FIRST_NAME_SESSION_ATTRIBUTE, simpleUser.firstName);
				httpSession.setAttribute(USER_LAST_NAME_SESSION_ATTRIBUTE, simpleUser.lastName);

				LOGGER.info("{} logged in", authentication.getName());

				super.onAuthenticationSuccess(request, response, authentication);
			}
		};
	}

}
