package uk.co.vhome.rmj.site.organiser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(value = "/organiser/event-registration", method = RequestMethod.POST)
	public String post()
	{
		return "redirect:/organiser/event-registration";
	}

	@RequestMapping(value = "/organiser/event-registration", method = RequestMethod.GET)
	public void get()
	{
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
