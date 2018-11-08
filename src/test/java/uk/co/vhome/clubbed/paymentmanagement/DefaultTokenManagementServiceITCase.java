package uk.co.vhome.clubbed.paymentmanagement;

import org.axonframework.boot.autoconfig.AxonAutoConfiguration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import uk.co.vhome.clubbed.InitialSiteUser;
import uk.co.vhome.clubbed.apiobjects.BalanceUpdatedEvent;
import uk.co.vhome.clubbed.apiobjects.LowBalanceEvent;
import uk.co.vhome.clubbed.entities.Order;
import uk.co.vhome.clubbed.entities.UserEntity;
import uk.co.vhome.clubbed.repositories.UsersRepository;
import uk.co.vhome.clubbed.usermanagement.DefaultUserAccountManagementService;
import uk.co.vhome.clubbed.usermanagement.UserAccountManagementService;

import javax.sql.DataSource;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static uk.co.vhome.clubbed.test.UserConfigurations.ENABLED_USER;

// Prevent DB migrations from taking place
@DataJpaTest(excludeAutoConfiguration = {FlywayAutoConfiguration.class, AxonAutoConfiguration.class}, showSql = false)
// Change the default 'create-drop' strategy from to avoid a Derby specific issue (https://github.com/spring-projects/spring-boot/issues/7706)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=none")
@Import(DefaultTokenManagementService.class)
class DefaultTokenManagementServiceITCase
{
	@TestConfiguration
	static class Configuration
	{
		@Bean
		public MethodValidationPostProcessor methodValidationPostProcessor() {
			return new MethodValidationPostProcessor();
		}

		@Bean
		SessionRegistry sessionRegistry()
		{
			return Mockito.mock(SessionRegistry.class);
		}

		@Bean
		public EventBus mockEventBus()
		{
			return mock(EventBus.class);
		}

		@Bean
		InitialSiteUser initialSiteUser()
		{
			return new InitialSiteUser("", "", "", "");
		}

		@Bean
		JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource)
		{
			JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();

			jdbcUserDetailsManager.setEnableGroups(true);
			jdbcUserDetailsManager.setEnableAuthorities(false);
			jdbcUserDetailsManager.setDataSource(dataSource);

			return jdbcUserDetailsManager;
		}

		@Bean
		UserAccountManagementService userAccountManagementService(UsersRepository usersRepository,
		                                                                JdbcUserDetailsManager jdbcUserDetailsManager)
		{
			return new DefaultUserAccountManagementService(initialSiteUser(), jdbcUserDetailsManager, usersRepository, null,
			                                               sessionRegistry(), mockEventBus());
		}
	}

	@Autowired
	private UserAccountManagementService userAccountManagementService;

	@Autowired
	private TokenManagementService tokenManagementService;

	@Autowired
	private EventBus mockEventBus;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@AfterEach
	void teardown()
	{
		reset(mockEventBus);
	}

	@Test
	@Sql({"/user-schema.sql", "/test-users.sql", "/orders-schema.sql"})
	void returnsCorrectBalanceForCredit()
	{
		UserEntity enabledUser = userAccountManagementService.findUser(ENABLED_USER.getId());
		assertEquals(BigDecimal.valueOf(0), enabledUser.getUserDetailsEntity().getBalance());

		Order order = tokenManagementService.creditAccount(ENABLED_USER.getId(), 3L);
		assertNotNull(order);

		enabledUser = userAccountManagementService.findUser(ENABLED_USER.getId());

		assertEquals(BigDecimal.valueOf(3), enabledUser.getUserDetailsEntity().getBalance());

		ArgumentCaptor<EventMessage> eventMessage = ArgumentCaptor.forClass(EventMessage.class);
		verify(mockEventBus).publish(eventMessage.capture());
		assertTrue(eventMessage.getValue().getPayload() instanceof BalanceUpdatedEvent);
	}

	@Test
	@Sql({"/user-schema.sql", "/test-users.sql", "/orders-schema.sql", "/credit-user.sql"})
	void returnsCorrectBalanceForDebit()
	{
		Order order = tokenManagementService.debitAccount(ENABLED_USER.getId(), 1L);
		assertNotNull(order);

		UserEntity enabledUser = userAccountManagementService.findUser(ENABLED_USER.getId());

		assertEquals(BigDecimal.valueOf(5), enabledUser.getUserDetailsEntity().getBalance());

		ArgumentCaptor<EventMessage> eventMessage = ArgumentCaptor.forClass(EventMessage.class);
		verify(mockEventBus).publish(eventMessage.capture());
		assertTrue(eventMessage.getValue().getPayload() instanceof BalanceUpdatedEvent);
	}

	@Test
	@Sql({"/user-schema.sql", "/test-users.sql", "/orders-schema.sql", "/credit-user.sql"})
	void sendsLowBalanceNotification()
	{
		Order order = tokenManagementService.debitAccount(ENABLED_USER.getId(), 4L);
		assertNotNull(order);

		UserEntity enabledUser = userAccountManagementService.findUser(ENABLED_USER.getId());

		assertEquals(BigDecimal.valueOf(2), enabledUser.getUserDetailsEntity().getBalance());

		ArgumentCaptor<EventMessage> eventMessage = ArgumentCaptor.forClass(EventMessage.class);
		verify(mockEventBus).publish(eventMessage.capture());
		assertTrue(eventMessage.getValue().getPayload() instanceof LowBalanceEvent);
	}

	@Test
	@Sql({"/user-schema.sql", "/test-users.sql", "/orders-schema.sql", "/credit-user.sql"})
	void throwsRuntimeExceptionForInvalidUser()
	{
		assertThrows(RuntimeException.class, () -> tokenManagementService.creditAccount(1000L, 4L));

		assertThrows(RuntimeException.class, () -> tokenManagementService.debitAccount(1000L, 4L));

		verifyZeroInteractions(mockEventBus);
	}

	@Test
	void throwsCreditConstraintViolationWhenCreditQuantityIsZero()
	{
		assertThrows(ConstraintViolationException.class, () -> tokenManagementService.creditAccount(ENABLED_USER.getId(), 0L));
	}

	@Test
	void throwsCreditConstraintViolationWhenDebitQuantityIsZero()
	{
		assertThrows(ConstraintViolationException.class, () -> tokenManagementService.debitAccount(ENABLED_USER.getId(), 0L));
	}

}
