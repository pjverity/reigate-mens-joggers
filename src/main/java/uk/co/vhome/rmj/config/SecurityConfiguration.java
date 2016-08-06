package uk.co.vhome.rmj.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uk.co.vhome.rmj.services.UserService;

import javax.inject.Inject;

import static uk.co.vhome.rmj.config.BootstrapFramework.ADDITIONAL_RESOURCE_PATHS;

/*
 * Configure web security for the site
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, order = 0)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final UserService userService;

	@Inject
	public SecurityConfiguration(UserService userService)
	{
		this.userService = userService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		LOGGER.info("Configuring authentication...");

		auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
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
}
