package uk.co.vhome.rmj.site.organiser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.services.EventManagementService;
import uk.co.vhome.rmj.services.TokenManagementService;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EventRegistrationViewController
{
	private final TokenManagementService tokenManagementService;

	private final EventManagementService eventManagementService;

	@Inject
	public EventRegistrationViewController(TokenManagementService tokenManagementService,
	                                       EventManagementService eventManagementService)
	{
		this.tokenManagementService = tokenManagementService;
		this.eventManagementService = eventManagementService;
	}

	@PostMapping(value = "/organiser/event-registration")
	public String post(EventRegistrationFormObject eventRegistrationFormObject)
	{
		// TODO - this should be done in a single transaction

		eventRegistrationFormObject.getRows().stream()
				.filter(EventRegistrationFormRow::isPresent)
				.forEach(r -> decrementBalance(r.getMemberBalance().getUsername()));

		eventManagementService.completeEvent(eventRegistrationFormObject.getEvent());

		return "redirect:/organiser/event-registration";
	}

	@GetMapping(value = "/organiser/event-registration")
	public void get()
	{
	}

	@ModelAttribute
	public EventRegistrationFormObject eventRegistrationFormObject()
	{
		EventRegistrationFormObject formObject = new EventRegistrationFormObject();

		List<EventRegistrationFormRow> rows = tokenManagementService.balanceForAllEnabledMembers()
				                                      .stream()
				                                      .sorted(Comparator.comparing(MemberBalance::getLastName).thenComparing(Comparator.comparing(MemberBalance::getFirstName)))
				                                      .map(EventRegistrationFormRow::new)
				                                      .collect(Collectors.toList());
		formObject.setRows(rows);

		return formObject;
	}

	@ModelAttribute("events")
	public List<Event> events()
	{
		return eventManagementService.findAllIncompleteEvents();
	}

	private void decrementBalance(String username)
	{
		tokenManagementService.debitAccount(username, 1);
	}

}
