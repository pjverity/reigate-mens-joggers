package uk.co.vhome.clubbed.web.site.organiser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.clubbed.entities.Event;
import uk.co.vhome.clubbed.entities.UserDetailsEntity;
import uk.co.vhome.clubbed.entities.UserEntity;
import uk.co.vhome.clubbed.eventmanagement.EventManagementService;
import uk.co.vhome.clubbed.paymentmanagement.TokenManagementService;
import uk.co.vhome.clubbed.usermanagement.Group;
import uk.co.vhome.clubbed.usermanagement.UserAccountManagementService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DefaultEventRegistrationService implements EventRegistrationService
{
	private final TokenManagementService tokenManagementService;

	private final EventManagementService eventManagementService;

	private final UserAccountManagementService userAccountManagementService;

	@Autowired
	public DefaultEventRegistrationService(TokenManagementService tokenManagementService, EventManagementService eventManagementService, UserAccountManagementService userAccountManagementService)
	{
		this.tokenManagementService = tokenManagementService;
		this.eventManagementService = eventManagementService;
		this.userAccountManagementService = userAccountManagementService;
	}

	@Override
	@Transactional
	public void completeEventAndDebitMemberAccounts(Event event, Collection<Long> userIds)
	{
		List<UserEntity> userDetailsEntities = userAccountManagementService.findUsers(userIds);

		event.setUserDetailsEntities(userDetailsEntities.stream().map(UserEntity::getUserDetailsEntity).collect(Collectors.toList()));

		userDetailsEntities.forEach(u -> tokenManagementService.debitAccount(u.getId(), 1L));

		eventManagementService.completeEvent(event);
	}

	@Override
	public Stream<UserDetailsEntity> fetchMemberBalances()
	{
		return userAccountManagementService.enabledUsersInGroup(true, Group.MEMBER)
				                       .stream()
				                       .map(UserEntity::getUserDetailsEntity)
				                       .sorted(UserDetailsEntity.LAST_NAME_FIRST_NAME_SORT);
	}

	@Override
	public List<Event> fetchIncompleteEvents()
	{
		return eventManagementService.findAllIncompleteEvents();
	}

	@Override
	public List<Event> fetchIncompleteEventsOnOrBeforeToday()
	{
		return eventManagementService.fetchEventsBefore(LocalDateTime.now(), true, false);
	}
}
