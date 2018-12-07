package uk.co.vhome.clubbed.web.site.organiser.services;

import uk.co.vhome.clubbed.entities.Event;
import uk.co.vhome.clubbed.entities.UserDetailsEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface EventRegistrationService
{
	void completeEventAndDebitMemberAccounts(Event event, Collection<Long> userIds);

	Stream<UserDetailsEntity> fetchMemberBalances();

	List<Event> fetchIncompleteEvents();

	List<Event> fetchIncompleteEventsOnOrBeforeToday();
}
