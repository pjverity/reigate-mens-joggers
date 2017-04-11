package uk.co.vhome.rmj.site.organiser;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.services.controller.EventRegistrationService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
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
	public String post(@Valid EventRegistrationFormObject eventRegistrationFormObject, BindingResult bindingResult)
	{
		if ( bindingResult.hasErrors() )
		{
			return "/organiser/event-registration";
		}

		Set<String> usernames = eventRegistrationFormObject.getRows().stream()
				                        .filter(EventRegistrationFormRow::isPresent)
				                        .map(r -> r.getMemberBalance().getUsername())
				                        .collect(Collectors.toSet());


		Distance distanceInGivenMetric = new Distance(eventRegistrationFormObject.getDistance(),
		                                              eventRegistrationFormObject.getMetric());

		BigDecimal distanceInKm = BigDecimal.valueOf(distanceInGivenMetric.in(Metrics.KILOMETERS).getValue());

		Event event = eventRegistrationFormObject.getEvent();
		event.getEventInfo().setDistance(distanceInKm);

		eventRegistrationService.completeEventAndDebitMemberAccounts(event, usernames);

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

	@ModelAttribute("distances")
	public List<Double> distances()
	{
		return Arrays.asList(2.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 15.0, 20.0);
	}

}
