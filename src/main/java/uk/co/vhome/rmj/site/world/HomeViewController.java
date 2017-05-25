package uk.co.vhome.rmj.site.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import uk.co.vhome.rmj.config.ServletContextConfiguration;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.site.world.services.HomeViewControllerService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The sites home page that also provides user registration functions
 */
@Controller
public class HomeViewController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String VIEW_NAME = "world/home";

	private static final String MESSAGE_CODE_VALIDATION_CONSTRAINT_USER_REGISTRATION_VALID = "validation.constraint.UserRegistrationValid";

	private final MessageSource messageSource;

	private final HomeViewControllerService homeViewControllerService;

	private static class ConciseFieldError
	{
		private final String field;

		private final String defaultMessage;

		ConciseFieldError(FieldError fieldError)
		{
			field = fieldError.getField();
			defaultMessage = fieldError.getDefaultMessage();
		}

		@SuppressWarnings("unused") // Used via introspection during de/serialisation
		public String getField()
		{
			return field;
		}

		@SuppressWarnings("unused") // Used via introspection during de/serialisation
		public String getDefaultMessage()
		{
			return defaultMessage;
		}
	}

	@Inject
	public HomeViewController(HomeViewControllerService homeViewControllerService, MessageSource messageSource)
	{
		this.homeViewControllerService = homeViewControllerService;
		this.messageSource = messageSource;
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String get(ModelMap model, @CookieValue(name = "cookiesAccepted", defaultValue = "false") Boolean cookiesAccepted)
	{
		model.put("form", new UserRegistrationFormObject());
		model.put("cookiesAccepted", cookiesAccepted);

		return VIEW_NAME;
	}

	@RequestMapping(path = "/register", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap registerNewMember(@ModelAttribute("form") @Valid UserRegistrationFormObject userRegistrationFormObject,
	                                  BindingResult errors, HttpServletRequest httpServletRequest) throws IOException
	{

		ModelMap model = new ModelMap();

		if (errors.hasErrors())
		{
			LOGGER.error("Validation failed for user registration: {}", userRegistrationFormObject);

			List<ConciseFieldError> conciseFieldErrors = errors.getFieldErrors().stream()
					                                             .map(ConciseFieldError::new)
					                                             .collect(Collectors.toList());

			String message;

			ObjectError globalError = errors.getGlobalError();
			if (globalError == null)
			{
				message = messageSource.getMessage(MESSAGE_CODE_VALIDATION_CONSTRAINT_USER_REGISTRATION_VALID, null, Locale.getDefault());
			} else
			{
				message = globalError.getDefaultMessage();
			}

			populatePageModelForRegistration(model, false, conciseFieldErrors, message);

			return model;
		}

		try
		{
			UserDetailsEntity userDetailsEntity = homeViewControllerService.registerNewUser(userRegistrationFormObject.getEmailAddress(),
			                                                                                userRegistrationFormObject.getFirstName(),
			                                                                                userRegistrationFormObject.getLastName(),
			                                                                                userRegistrationFormObject.getPassword());

			// This appears to by-pass the SecurityConfiguration.authenticationSuccessHandler() handler, so have to duplicate
			// setting the session variables here
			httpServletRequest.login(userDetailsEntity.getUsername(), userRegistrationFormObject.getPassword());
			HttpSession httpSession = httpServletRequest.getSession();
			httpSession.setAttribute(ServletContextConfiguration.USER_ID_SESSION_ATTRIBUTE, userDetailsEntity.getId());
			httpSession.setAttribute(ServletContextConfiguration.USER_FIRST_NAME_SESSION_ATTRIBUTE, userDetailsEntity.getFirstName());
			httpSession.setAttribute(ServletContextConfiguration.USER_LAST_NAME_SESSION_ATTRIBUTE, userDetailsEntity.getLastName());

			populatePageModelForRegistration(model, true, null, null);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to register user", e);
			populatePageModelForRegistration(model, false, null, "Failed to register new user");
		}

		return model;
	}

	@SuppressWarnings("unused")
	@ModelAttribute("nextEvent")
	String nextEvent()
	{
		Optional<Event> optionalNextEvent = homeViewControllerService.findNextEvent();

		return optionalNextEvent.map(event -> event.getEventDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT)))
				       .orElse("Watch this space!");

	}

	private static void populatePageModelForRegistration(Map<String, Object> pageModel, boolean isSuccessful, List<ConciseFieldError> conciseFieldErrors, String generalErrorMessage)
	{
		pageModel.put("success", isSuccessful);
		pageModel.put("error", generalErrorMessage);
		pageModel.put("fieldErrors", conciseFieldErrors);
	}
}