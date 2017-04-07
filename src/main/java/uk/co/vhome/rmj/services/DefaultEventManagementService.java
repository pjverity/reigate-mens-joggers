package uk.co.vhome.rmj.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.entities.Event;
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
	public void createNewEvent(LocalDateTime eventDateTime)
	{
		Event newEvent = new Event();
		newEvent.setEventDateTime(eventDateTime);

		eventRepository.save(newEvent);
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
}
