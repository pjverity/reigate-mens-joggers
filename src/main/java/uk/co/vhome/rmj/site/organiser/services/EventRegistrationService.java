package uk.co.vhome.rmj.site.organiser.services;

import uk.co.vhome.clubbed.domainobjects.entities.Event;
import uk.co.vhome.rmj.entities.MemberBalance;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface EventRegistrationService
{
	void completeEventAndDebitMemberAccounts(Event event, Collection<Long> userIds);

	Stream<MemberBalance> fetchMemberBalances();

	List<Event> fetchIncompleteEvents();

	List<Event> fetchIncompleteEventsOnOrBeforeToday();
}
