package uk.co.vhome.rmj.services;

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
		LocalDateTime adjustedDateTime = inclusive ? dateTime.minusDays(1) : dateTime;

		return eventRepository.findAllByCompletedAndEventDateTimeAfter(completed, adjustedDateTime);
	}

	@Override
	public List<Event> fetchEventsBefore(LocalDateTime dateTime, boolean inclusive, boolean completed)
	{
		LocalDateTime adjustedDateTime = inclusive ? dateTime.plusDays(1) : dateTime;

		return eventRepository.findAllByCompletedAndEventDateTimeBefore(completed, adjustedDateTime);
	}
}
