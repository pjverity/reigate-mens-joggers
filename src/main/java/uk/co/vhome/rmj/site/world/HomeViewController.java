package uk.co.vhome.rmj.site.world;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.vhome.clubbed.web.controllers.userregistration.UserRegistrationFormObject;
import uk.co.vhome.rmj.services.core.EventManagementService;
import uk.co.vhome.rmj.services.flickr.FlickrService;

import javax.inject.Inject;
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

	@Inject
	public HomeViewController(EventManagementService eventManagementService, FlickrService flickrService)
	{
		this.eventManagementService = eventManagementService;
		this.flickrService = flickrService;
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
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