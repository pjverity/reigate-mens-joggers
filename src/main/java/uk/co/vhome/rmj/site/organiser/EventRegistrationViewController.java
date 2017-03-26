package uk.co.vhome.rmj.site.organiser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.co.vhome.rmj.services.TokenManagementService;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EventRegistrationViewController
{
	private final TokenManagementService tokenManagementService;

	@Inject
	public EventRegistrationViewController(TokenManagementService tokenManagementService)
	{
		this.tokenManagementService = tokenManagementService;
	}

	@PostMapping(value = "/organiser/event-registration")
	public String post(EventRegistrationFormObject eventRegistrationFormObject)
	{
		eventRegistrationFormObject.getRows().stream()
				.filter(EventRegistrationFormRow::isPresent)
				.forEach(r -> decrementBalance(r.getMemberBalance().getUsername()));

		return "redirect:/organiser/event-registration";
	}

	@GetMapping(value = "/organiser/event-registration")
	public void get()
	{
	}

	private void decrementBalance(String username)
	{
		tokenManagementService.debitAccount(username, 1);
	}

	@ModelAttribute
	public EventRegistrationFormObject eventRegistrationFormObject()
	{
		EventRegistrationFormObject formObject = new EventRegistrationFormObject();

		List<EventRegistrationFormRow> rows = tokenManagementService.balanceForAllEnabledMembers()
				                                         .stream()
				                                         .map(EventRegistrationFormRow::new)
				                                         .collect(Collectors.toList());
		formObject.setRows(rows);

		return formObject;
	}

}
