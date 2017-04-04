package uk.co.vhome.rmj.site.organiser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
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
import java.util.List;

@Controller
public class EventManagementViewController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String ERROR_CODE = "uk.co.vhome.rmj.site.organiser.EventManagementViewController.CreateEventFailed";

	private static final List<LocalTime> EVENT_TIMES = new ArrayList<>(13);

	private final EventManagementService eventManagementService;

	static
	{
		EVENT_TIMES.add(LocalTime.of(7, 0));
		EVENT_TIMES.add(LocalTime.of(8, 0));
		EVENT_TIMES.add(LocalTime.of(9, 0));
		EVENT_TIMES.add(LocalTime.of(10, 0));
		EVENT_TIMES.add(LocalTime.of(11, 0));
		EVENT_TIMES.add(LocalTime.of(12, 0));
		EVENT_TIMES.add(LocalTime.of(13, 0));
		EVENT_TIMES.add(LocalTime.of(14, 0));
		EVENT_TIMES.add(LocalTime.of(15, 0));
		EVENT_TIMES.add(LocalTime.of(16, 0));
		EVENT_TIMES.add(LocalTime.of(17, 0));
		EVENT_TIMES.add(LocalTime.of(18, 0));
		EVENT_TIMES.add(LocalTime.of(19, 0));
	}

	@Inject
	public EventManagementViewController(EventManagementService eventManagementService)
	{
		this.eventManagementService = eventManagementService;
	}

	@SuppressWarnings("unused")
	@GetMapping("/organiser/event-management")
	void get()
	{
	}

	@SuppressWarnings("unused")
	@PostMapping("/organiser/event-management")
	String post(@Valid EventManagementFormObject eventManagementFormObject, BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
		{
			return "/organiser/event-management";
		}

		try
		{

			LocalDateTime eventDateTime = LocalDateTime.of(eventManagementFormObject.getEventDate(),
			                                               eventManagementFormObject.getEventTime());

			eventManagementService.createNewEvent(eventDateTime);

			return "redirect:/organiser/event-management";
		}
		catch (DataIntegrityViolationException e)
		{
			bindingResult.reject(ERROR_CODE);
			LOGGER.error("Failed to create new event", e);
		}

		return "/organiser/event-management";
	}

	@SuppressWarnings("unused")
	@ModelAttribute("eventTimes")
	List<LocalTime> eventTimes()
	{
		return EVENT_TIMES;
	}

	@SuppressWarnings("unused")
	@ModelAttribute("incompleteEvents")
	List<Event> incompleteEvents()
	{
		return eventManagementService.findAllIncompleteEvents();
	}

	@SuppressWarnings("unused")
	@ModelAttribute
	EventManagementFormObject eventManagementFormObject()
	{
		EventManagementFormObject eventManagementFormObject = new EventManagementFormObject();
		eventManagementFormObject.setEventDate(LocalDate.now());
		return eventManagementFormObject;
	}
}
