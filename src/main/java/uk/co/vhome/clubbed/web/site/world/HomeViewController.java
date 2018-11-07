package uk.co.vhome.clubbed.web.site.world;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import uk.co.vhome.clubbed.eventmanagement.EventManagementService;
import uk.co.vhome.clubbed.flickrapi.FlickrService;
import uk.co.vhome.clubbed.web.controllers.userregistration.UserRegistrationFormObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Reigate Mens Joggers's landing page
 */
@Controller
public class HomeViewController
{
	private static final String VIEW_NAME = "world/home";

	private static final String NO_UPCOMING_RUN_MESSAGE = "Watch this space!";

	private final EventManagementService eventManagementService;

	private final FlickrService flickrService;

	@Autowired
	public HomeViewController(EventManagementService eventManagementService, FlickrService flickrService)
	{
		this.eventManagementService = eventManagementService;
		this.flickrService = flickrService;
	}

	@GetMapping(path = "/")
	public String get(ModelMap model, @CookieValue(name = "cookiesAccepted", defaultValue = "false") Boolean cookiesAccepted)
	{
		model.put("form", new UserRegistrationFormObject());
		model.put("cookiesAccepted", cookiesAccepted);

		return VIEW_NAME;
	}

	@SuppressWarnings("unused")
	@ModelAttribute("nextEvent")
	String nextEvent()
	{
		return eventManagementService.fetchEventsAfter(LocalDateTime.now(), true, false)
				       .stream()
				       .findFirst()
				       .map(event -> event.getEventDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT)))
				       .orElse(NO_UPCOMING_RUN_MESSAGE);
	}

	@ModelAttribute("flickGroupNsid")
	public String flickGroupNsid()
	{
		return flickrService.getCurrentGroupNsid();
	}

}