package uk.co.vhome.rmj.services.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.entities.EventInfo;
import uk.co.vhome.rmj.repositories.EventRepository;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefaultEventManagementService implements EventManagementService
{
	private final EventRepository eventRepository;

	@Inject
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
		EventInfo newEventInfo = new EventInfo();
		Event newEvent = new Event(eventDateTime, newEventInfo);
		newEventInfo.setEvent(newEvent);

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
