package uk.co.vhome.rmj.site.organiser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.services.EventManagementService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class EventSchedulingViewController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String VIEW_NAME = "/organiser/event-scheduling";

	private static final String ERROR_CODE = "uk.co.vhome.rmj.site.organiser.EventSchedulingViewController.CreateEventFailed";

	private final EventManagementService eventManagementService;

	@Inject
	public EventSchedulingViewController(EventManagementService eventManagementService)
	{
		this.eventManagementService = eventManagementService;
	}

	@SuppressWarnings("unused")
	@GetMapping(VIEW_NAME)
	void get(@Param(value = "eventId") Long eventId,
	         @Param(value = "cancelEventId") Long cancelEventId,
	         @ModelAttribute("completedEvents") ArrayList<Event> completedEvents,
	         @ModelAttribute EventCancellationFormObject eventCancellationFormObject, ModelMap modelMap)
	{
		if (eventId != null)
		{
			Optional<Event> event = completedEvents.stream().filter(e -> e.getId().equals(eventId)).findFirst();
			event.ifPresent(e -> modelMap.put("selectedEvent", e));
		}

		if (cancelEventId != null)
		{
			eventCancellationFormObject.getEvents().stream()
					.filter(e -> e.getId().equals(cancelEventId))
					.findFirst()
					.ifPresent(e -> e.setCancelled(true));
		}
	}

	@SuppressWarnings("unused")
	@PostMapping("/organiser/create-event")
	String createEvent(@Valid EventCreationFormObject eventCreationFormObject, BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
		{
			return VIEW_NAME;
		}

		try
		{
			LocalTime localTime = LocalTime.of(eventCreationFormObject.getEventHour(), eventCreationFormObject.getEventMinutes());
			LocalDateTime eventDateTime = LocalDateTime.of(eventCreationFormObject.getEventDate(), localTime);

			eventManagementService.createNewEvent(eventDateTime);

			return "redirect:" + VIEW_NAME;
		}
		catch (DataIntegrityViolationException e)
		{
			bindingResult.reject(ERROR_CODE);
			LOGGER.error("Failed to create new event", e);
		}

		return VIEW_NAME;
	}

	@SuppressWarnings("unused")
	@PostMapping("/organiser/cancel-event")
	String cancelEvent(EventCancellationFormObject eventCancellationFormObject)
	{
		eventCancellationFormObject.getEvents().stream()
				.filter(Event::isCancelled)
				.forEach(eventManagementService::cancelEvent);

		return "redirect:" + VIEW_NAME;
	}

	@SuppressWarnings("unused")
	@ModelAttribute("completedEvents")
	List<Event> completedEvents()
	{
		return eventManagementService.findTop10CompletedEvents();
	}

	@SuppressWarnings("unused")
	@ModelAttribute
	EventCreationFormObject eventCreationFormObject()
	{
		EventCreationFormObject eventCreationFormObject = new EventCreationFormObject();
		eventCreationFormObject.setEventDate(LocalDate.now());
		return eventCreationFormObject;
	}

	@SuppressWarnings("unused")
	@ModelAttribute
	EventCancellationFormObject eventCancellationFormObject()
	{
		EventCancellationFormObject eventCancellationFormObject = new EventCancellationFormObject();

		List<Event> allIncompleteEvents = eventManagementService.findAllIncompleteEvents();
		allIncompleteEvents.sort(Comparator.comparing(Event::getEventDateTime));
		eventCancellationFormObject.setEvents(allIncompleteEvents);

		return eventCancellationFormObject;
	}
}
