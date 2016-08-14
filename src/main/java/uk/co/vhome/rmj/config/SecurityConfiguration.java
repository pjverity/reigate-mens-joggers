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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

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

	@Bean
	JdbcUserDetailsManager jdbcUserDetailsManager()
	{
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
		userDetailsManager.setDataSource(springJpaDataSource);

		initialiseAdminUser(userDetailsManager);

		return userDetailsManager;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		LOGGER.info("Configuring authentication...");

		auth.userDetailsService(jdbcUserDetailsManager()).passwordEncoder(new BCryptPasswordEncoder());
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
					.defaultSuccessUrl("/")
					.usernameParameter("username")
					.passwordParameter("password")
					.permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception
	{
		LOGGER.info("Configuring Web security...");

		// Bypass the security filters for efficiency
		web.ignoring().antMatchers(ADDITIONAL_RESOURCE_PATHS);
	}

	private void initialiseAdminUser(JdbcUserDetailsManager userDetailsManager)
	{
		if ( !userDetailsManager.userExists("admin") )
		{
			LOGGER.warn("Generating default admin user. Log in as admin user and change the default password");

			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ADMIN");
			userDetailsManager.createUser(new User("admin", BCrypt.hashpw("test", BCrypt.gensalt()), Collections.singleton(authority)));
		}
	}
}
