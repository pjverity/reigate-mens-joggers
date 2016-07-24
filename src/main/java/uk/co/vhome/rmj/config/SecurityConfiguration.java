package uk.co.vhome.rmj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static uk.co.vhome.rmj.config.BootstrapFramework.ADDITIONAL_RESOURCE_PATHS;

/*
 * Configure web security for the site
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.inMemoryAuthentication()
				.withUser("paul")
				.password("test")
				.authorities("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
				.antMatchers("/admin/**").authenticated()
				.antMatchers("/**").permitAll()
				.and().formLogin()
				.defaultSuccessUrl("/admin/")
				.usernameParameter("username")
				.passwordParameter("password")
				.permitAll()
				.and()
				.csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception
	{
		// Bypass the security filters for efficiency
		web.ignoring().antMatchers(ADDITIONAL_RESOURCE_PATHS);
	}
}
