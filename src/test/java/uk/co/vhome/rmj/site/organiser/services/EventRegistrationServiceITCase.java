package uk.co.vhome.rmj.site.organiser.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.clubbed.domainobjects.entities.Event;
import uk.co.vhome.clubbed.web.services.UserAccountManagementService;
import uk.co.vhome.rmj.IntegrationTestConfiguration;
import uk.co.vhome.rmj.services.core.DefaultEventManagementService;
import uk.co.vhome.rmj.services.core.DefaultTokenManagementService;
import uk.co.vhome.rmj.services.core.TokenManagementService;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static uk.co.vhome.rmj.UserConfigurations.ENABLED_USER;
import static uk.co.vhome.rmj.UserConfigurations.ENABLED_USER_ID;

@RunWith(SpringRunner.class)
@ActiveProfiles({"integration-test"})
@ContextConfiguration(classes = {
		IntegrationTestConfiguration.class,
		DefaultTokenManagementService.class,
		DefaultEventManagementService.class,
		DefaultEventRegistrationService.class
})
@Transactional
public class EventRegistrationServiceITCase
{
	@Inject
	private EventRegistrationService eventRegistrationService;

	@Inject
	private TokenManagementService tokenManagementService;

	@Inject
	private UserAccountManagementService mockUserAccountManagementService;

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void completingEventDebitsUserAccount() throws Exception
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		Set<Long> userIds = new HashSet<>();
		userIds.add(ENABLED_USER_ID);

		int initialBalance = tokenManagementService.balanceForMember(ENABLED_USER_ID);

		List<Event> incompleteEvents = eventRegistrationService.fetchIncompleteEvents();

		Event eventToComplete = incompleteEvents.get(0);

		eventRegistrationService.completeEventAndDebitMemberAccounts(eventToComplete, userIds);

		int finalBalance = tokenManagementService.balanceForMember(ENABLED_USER_ID);

		assertEquals(initialBalance-1, finalBalance);

	}
}
