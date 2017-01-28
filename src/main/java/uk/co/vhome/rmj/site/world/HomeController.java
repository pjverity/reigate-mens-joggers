package uk.co.vhome.rmj.site.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.co.vhome.rmj.config.ServletContextConfiguration;
import uk.co.vhome.rmj.services.UserRegistrationService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The sites home page that also provides user registration functions
 */
@Controller
public class HomeController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String VIEW_NAME = "world/home";

	private static final String MESSAGE_CODE_VALIDATION_CONSTRAINT_USER_REGISTRATION_VALID = "validation.constraint.UserRegistrationValid";

	private final UserRegistrationService registrationService;

	private final MessageSource messageSource;

	private static class ConciseFieldError
	{
		private final String field;

		private final String defaultMessage;

		ConciseFieldError(FieldError fieldError)
		{
			field = fieldError.getField();
			defaultMessage = fieldError.getDefaultMessage();
		}

		public String getField()
		{
			return field;
		}

		public String getDefaultMessage()
		{
			return defaultMessage;
		}
	}

	@Inject
	public HomeController(UserRegistrationService registrationService, MessageSource messageSource)
	{
		this.registrationService = registrationService;
		this.messageSource = messageSource;
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String get(ModelMap model)
	{
		model.put("form", new UserRegistrationFormObject());
		model.put("registrationServiceAvailable", registrationService.isServiceAvailable());

		return VIEW_NAME;
	}

	@RequestMapping(path = "/register", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> registerNewMember(@ModelAttribute("form") @Valid UserRegistrationFormObject userRegistrationFormObject,
	                                             BindingResult errors, ModelMap model, HttpServletRequest httpServletRequest) throws IOException
	{
		Map<String, Object> pageModel = new HashMap<>();

		if (errors.hasErrors())
		{
			LOGGER.error("Validation failed for user registration: {}", userRegistrationFormObject);

			List<ConciseFieldError> conciseFieldErrors = errors.getFieldErrors().stream()
					.map(ConciseFieldError::new)
					.collect(Collectors.toList());

			String message;

			ObjectError globalError = errors.getGlobalError();
			if ( globalError == null )
			{
				message = messageSource.getMessage(MESSAGE_CODE_VALIDATION_CONSTRAINT_USER_REGISTRATION_VALID, null, Locale.getDefault());
			}
			else
			{
				message = globalError.getDefaultMessage();
			}

			populatePageModelForRegistration(pageModel, false, conciseFieldErrors, message);

			return pageModel;
		}

		try
		{
			registrationService.registerNewUser(userRegistrationFormObject.getEmailAddress(),
					userRegistrationFormObject.getFirstName(),
					userRegistrationFormObject.getLastName(),
					userRegistrationFormObject.getPassword()
			);

			// This appears to by-pass the login handler, so have to duplicate setting the session variables here
			httpServletRequest.login(userRegistrationFormObject.getConfirmEmailAddress(), userRegistrationFormObject.getPassword());
			HttpSession httpSession = httpServletRequest.getSession();
			httpSession.setAttribute(ServletContextConfiguration.USER_FIRST_NAME_SESSION_ATTRIBUTE, StringUtils.capitalize(userRegistrationFormObject.getFirstName()));
			httpSession.setAttribute(ServletContextConfiguration.USER_LAST_NAME_SESSION_ATTRIBUTE, StringUtils.capitalize(userRegistrationFormObject.getLastName()));

			populatePageModelForRegistration(pageModel, true, null, null);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to register user", e);
			populatePageModelForRegistration(pageModel, false, null, "Failed to register new user");
		}

		return pageModel;
	}

	private static void populatePageModelForRegistration(Map<String, Object> pageModel, boolean isSuccessful, List<ConciseFieldError> conciseFieldErrors, String generalErrorMessage)
	{
		pageModel.put("success", isSuccessful);
		pageModel.put("error", generalErrorMessage);
		pageModel.put("fieldErrors", conciseFieldErrors);
	}
}