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
public class EventCompletionViewController
{
	private static final String VIEW_NAME = "/organiser/event-completion";

	private final EventRegistrationService eventRegistrationService;

	@Inject
	public EventCompletionViewController(EventRegistrationService eventRegistrationService)
	{
		this.eventRegistrationService = eventRegistrationService;
	}

	@GetMapping(VIEW_NAME)
	public void get()
	{
	}

	@PostMapping(VIEW_NAME)
	public String completeEvent(@Valid EventCompletionFormObject eventCompletionFormObject, BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
		{
			return VIEW_NAME;
		}

		Set<Long> userIds = eventCompletionFormObject.getRows().stream()
				                    .filter(EventCompletionFormRow::isPresent)
				                    .map(r -> r.getMemberBalance().getUserId())
				                    .collect(Collectors.toSet());


		Distance distanceInGivenMetric = new Distance(eventCompletionFormObject.getDistance(),
		                                              eventCompletionFormObject.getMetric());

		BigDecimal distanceInKm = BigDecimal.valueOf(distanceInGivenMetric.in(Metrics.KILOMETERS).getValue());

		Event event = eventCompletionFormObject.getEvent();
		event.getEventInfo().setDistance(distanceInKm);

		eventRegistrationService.completeEventAndDebitMemberAccounts(event, userIds);

		return "redirect:" + VIEW_NAME;
	}


	@ModelAttribute
	public EventCompletionFormObject eventRegistrationFormObject()
	{
		List<EventCompletionFormRow> rows = eventRegistrationService.fetchMemberBalances()
				                                    .map(EventCompletionFormRow::new)
				                                    .collect(Collectors.toList());

		return new EventCompletionFormObject(rows);
	}

	@ModelAttribute("events")
	public List<Event> events()
	{
		return eventRegistrationService.fetchIncompleteEventsOnOrBeforeToday();
	}

}
