package uk.co.vhome.rmj.site.organiser.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.repositories.UserDetailsRepository;
import uk.co.vhome.rmj.services.core.EventManagementService;
import uk.co.vhome.rmj.services.core.TokenManagementService;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DefaultEventRegistrationService implements EventRegistrationService
{
	private final TokenManagementService tokenManagementService;

	private final EventManagementService eventManagementService;

	private final UserDetailsRepository userDetailsRepository;

	@Inject
	public DefaultEventRegistrationService(TokenManagementService tokenManagementService, EventManagementService eventManagementService, UserDetailsRepository userDetailsRepository)
	{
		this.tokenManagementService = tokenManagementService;
		this.eventManagementService = eventManagementService;
		this.userDetailsRepository = userDetailsRepository;
	}

	@Override
	@Transactional
	public void completeEventAndDebitMemberAccounts(Event event, Collection<Long> userIds)
	{
		List<UserDetailsEntity> userDetailsEntities = userDetailsRepository.findAll(userIds);

		event.setUserDetailsEntities(userDetailsEntities);

		userDetailsEntities.forEach(u -> tokenManagementService.debitAccount(u.getId(), 1));

		eventManagementService.completeEvent(event);
	}

	@Override
	public Stream<MemberBalance> fetchMemberBalances()
	{
		return tokenManagementService.balancesForAllEnabledMembers().stream()
				.sorted(Comparator.comparing(MemberBalance::getLastName)
						        .thenComparing(Comparator.comparing(MemberBalance::getFirstName)));
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
