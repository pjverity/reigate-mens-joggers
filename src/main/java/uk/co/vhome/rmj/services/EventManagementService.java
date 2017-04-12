package uk.co.vhome.rmj.services;

import uk.co.vhome.rmj.entities.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventManagementService
{
	List<Event> findAllIncompleteEvents();

	List<Event> findTop10CompletedEvents();

	Event createNewEvent(LocalDateTime eventDateTime);

	void completeEvent(Event event);

	void cancelEvent(Event event);

	List<Event> fetchEventsAfter(LocalDateTime dateTime, boolean inclusive, boolean completed);

	List<Event> fetchEventsBefore(LocalDateTime dateTime, boolean inclusive, boolean completed);

}
