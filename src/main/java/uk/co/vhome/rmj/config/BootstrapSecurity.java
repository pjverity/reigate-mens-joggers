package uk.co.vhome.rmj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configure wecurity for the site
 */
@Configuration
@EnableWebSecurity
@Order(2)
public class BootstrapSecurity extends WebSecurityConfigurerAdapter
{
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		super.configure(auth);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		super.configure(http);
	}

	@Override
	public void configure(WebSecurity web) throws Exception
	{
		super.configure(web);
	}
}
