package uk.co.vhome.rmj.site.organiser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.services.controller.EventRegistrationService;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class EventRegistrationViewController
{

	private final EventRegistrationService eventRegistrationService;

	@Inject
	public EventRegistrationViewController(EventRegistrationService eventRegistrationService)
	{
		this.eventRegistrationService = eventRegistrationService;
	}

	@PostMapping(value = "/organiser/event-registration")
	public String post(EventRegistrationFormObject eventRegistrationFormObject)
	{
		Set<String> usernames = eventRegistrationFormObject.getRows().stream()
				                        .filter(EventRegistrationFormRow::isPresent)
				                        .map(r -> r.getMemberBalance().getUsername())
				                        .collect(Collectors.toSet());

		eventRegistrationService.completeEventAndDebitMemberAccounts(eventRegistrationFormObject.getEvent(), usernames);

		return "redirect:/organiser/event-registration";
	}

	@GetMapping(value = "/organiser/event-registration")
	public void get()
	{
	}

	@ModelAttribute
	public EventRegistrationFormObject eventRegistrationFormObject()
	{
		List<EventRegistrationFormRow> rows = eventRegistrationService.fetchMemberBalances()
				                                      .map(EventRegistrationFormRow::new)
				                                      .collect(Collectors.toList());

		return new EventRegistrationFormObject(rows);
	}

	@ModelAttribute("events")
	public List<Event> events()
	{
		return eventRegistrationService.fetchIncompleteEvents();
	}

}
