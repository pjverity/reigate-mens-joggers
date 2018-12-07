package uk.co.vhome.clubbed;

import org.axonframework.eventhandling.EventBus;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.sql.DataSource;

@TestConfiguration
public class JpaITConfiguration
{
	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource)
	{
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();

		jdbcUserDetailsManager.setEnableGroups(true);
		jdbcUserDetailsManager.setEnableAuthorities(false);
		jdbcUserDetailsManager.setDataSource(dataSource);

		return jdbcUserDetailsManager;
	}

	@Bean
	InitialSiteUser initialSiteUser()
	{
		return new InitialSiteUser("initial.user@home.co.uk",
		                           "Initial",
		                           "User",
		                           "password");
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	@Bean
	EventBus mockEventBus()
	{
		return Mockito.mock(EventBus.class);
	}

	@Bean
	SessionRegistry sessionRegistry()
	{
		return Mockito.mock(SessionRegistry.class);
	}

}
