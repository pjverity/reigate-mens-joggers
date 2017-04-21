package uk.co.vhome.rmj.services;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.security.Role;

import java.time.LocalDateTime;
import java.util.List;

public interface EventManagementService
{
	@Secured({Role.ADMIN})
	Event createNewEvent(LocalDateTime eventDateTime);

	@Secured({Role.ADMIN})
	void completeEvent(Event event);

	@Secured({Role.ADMIN})
	void cancelEvent(Event event);

	List<Event> findAllIncompleteEvents();

	List<Event> findTop10CompletedEvents();

	List<Event> fetchEventsAfter(LocalDateTime dateTime, boolean inclusive, boolean completed);

	List<Event> fetchEventsBefore(LocalDateTime dateTime, boolean inclusive, boolean completed);
}
