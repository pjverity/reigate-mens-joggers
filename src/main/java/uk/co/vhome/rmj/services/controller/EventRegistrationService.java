package uk.co.vhome.rmj.services.controller;

import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.entities.MemberBalance;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface EventRegistrationService
{
	void completeEventAndDebitMemberAccounts(Event event, Collection<String> usernames);

	Stream<MemberBalance> fetchMemberBalances();

	List<Event> fetchIncompleteEvents();

	List<Event> fetchIncompleteEventsOnOrBeforeToday();
}
