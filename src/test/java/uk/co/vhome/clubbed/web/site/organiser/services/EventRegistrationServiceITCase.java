package uk.co.vhome.clubbed.web.site.organiser.services;

import org.axonframework.boot.autoconfig.AxonAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import uk.co.vhome.clubbed.JpaITConfiguration;
import uk.co.vhome.clubbed.entities.Event;
import uk.co.vhome.clubbed.entities.UserDetailsEntity;
import uk.co.vhome.clubbed.eventmanagement.DefaultEventManagementService;
import uk.co.vhome.clubbed.paymentmanagement.DefaultTokenManagementService;
import uk.co.vhome.clubbed.usermanagement.DefaultUserAccountManagementService;
import uk.co.vhome.clubbed.usermanagement.UserAccountManagementService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static uk.co.vhome.clubbed.UserConfigurations.ENABLED_USER;

@DataJpaTest(excludeAutoConfiguration = {FlywayAutoConfiguration.class, AxonAutoConfiguration.class}, showSql = false)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=none")
@Import({JpaITConfiguration.class, DefaultTokenManagementService.class, DefaultEventManagementService.class, DefaultEventRegistrationService.class, DefaultUserAccountManagementService.class})
class EventRegistrationServiceITCase
{
	@Autowired
	private EventRegistrationService eventRegistrationService;

	@Autowired
	private UserAccountManagementService userAccountManagementService;

	@Test
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql", "/orders-schema.sql"})
	void completingEventDebitsUserAccount()
	{
		Set<Long> userIds = new HashSet<>();
		userIds.add(ENABLED_USER.getId());

		UserDetailsEntity userDetailsEntity = userAccountManagementService.findUser(ENABLED_USER.getId()).getUserDetailsEntity();

		BigDecimal initialBalance = userDetailsEntity.getBalance();

		List<Event> incompleteEvents = eventRegistrationService.fetchIncompleteEvents();

		Event eventToComplete = incompleteEvents.get(0);

		eventRegistrationService.completeEventAndDebitMemberAccounts(eventToComplete, userIds);

		userDetailsEntity = userAccountManagementService.findUser(ENABLED_USER.getId()).getUserDetailsEntity();

		BigDecimal finalBalance = userDetailsEntity.getBalance();

		Assertions.assertEquals( initialBalance.subtract(BigDecimal.ONE), finalBalance);
	}
}
