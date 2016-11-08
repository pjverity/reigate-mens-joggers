package uk.co.vhome.rmj.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.inject.Inject;
import javax.sql.DataSource;
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

	private final DataSource springJpaDataSource;

	@Inject
	public SecurityConfiguration(DataSource springJpaDataSource)
	{
		this.springJpaDataSource = springJpaDataSource;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		LOGGER.info("Configuring authentication...");

		// Store users in a database using a JDBC datasource to connect to it. So that passwords are not
		// stored in the database in plain text, they are hashed using the given password encoder
		auth.jdbcAuthentication()
				.dataSource(springJpaDataSource)
				.passwordEncoder(new BCryptPasswordEncoder());

		// If the database is empty, set up a default admin account
		initialiseAdminUser((UserDetailsManager)auth.getDefaultUserDetailsService());
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
