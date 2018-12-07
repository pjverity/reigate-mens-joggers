package uk.co.vhome.clubbed.eventmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.clubbed.repositories.EventRepository;
import uk.co.vhome.clubbed.entities.Event;
import uk.co.vhome.clubbed.entities.EventDetails;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefaultEventManagementService implements EventManagementService
{
	private final EventRepository eventRepository;

	@Autowired
	public DefaultEventManagementService(EventRepository eventRepository)
	{
		this.eventRepository = eventRepository;
	}

	@Override
	public List<Event> findAllIncompleteEvents()
	{
		return eventRepository.findAllByCompletedFalseOrderByEventDateTime();
	}

	@Override
	@Transactional
	public List<Event> findTop10CompletedEvents()
	{
		List<Event> events = eventRepository.findTop10ByCompletedTrueOrderByEventDateTimeDesc();
		events.forEach(e -> e.getUserDetailsEntities().size());
		return events;
	}

	@Override
	public Event createNewEvent(LocalDateTime eventDateTime)
	{
		EventDetails newEventDetails = new EventDetails();
		Event newEvent = new Event(eventDateTime, newEventDetails);
		newEventDetails.setEvent(newEvent);

		return eventRepository.save(newEvent);
	}

	@Override
	public void completeEvent(Event event)
	{
		event.setCompleted(true);
		eventRepository.save(event);
	}

	@Override
	public void cancelEvent(Event event)
	{
		if ( event.isCompleted() )
		{
			throw new IllegalArgumentException("Attempted to cancel a completed event");
		}

		eventRepository.delete(event);
	}

	@Override
	public List<Event> fetchEventsAfter(LocalDateTime dateTime, boolean inclusive, boolean completed)
	{
		if (inclusive)
		{
			return eventRepository.findAllByCompletedAndEventDateTimeAfterOrEventDateTime(completed, dateTime, dateTime);
		}

		return eventRepository.findAllByCompletedAndEventDateTimeAfter(completed, dateTime);
	}

	@Override
	public List<Event> fetchEventsBefore(LocalDateTime dateTime, boolean inclusive, boolean completed)
	{
		if (inclusive)
		{
			return eventRepository.findAllByCompletedAndEventDateTimeBeforeOrEventDateTime(completed, dateTime, dateTime);
		}

		return eventRepository.findAllByCompletedAndEventDateTimeBefore(completed, dateTime);
	}
}
