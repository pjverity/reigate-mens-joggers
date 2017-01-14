package uk.co.vhome.rmj.site.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import uk.co.vhome.rmj.services.UserRegistrationService;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The sites home page that also provides user registration functions
 */
@Controller
@SessionAttributes({"registrationEmail"})
public class HomeController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String VIEW_NAME = "world/home";

	private final UserRegistrationService registrationService;

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
	public HomeController(UserRegistrationService registrationService)
	{
		this.registrationService = registrationService;
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
	                                             BindingResult errors, ModelMap model) throws IOException
	{
		Map<String, Object> pageModel = new HashMap<>();

		if (errors.hasErrors())
		{
			LOGGER.error("Validation failed for user registration: {}", userRegistrationFormObject);

			List<ConciseFieldError> conciseFieldErrors = errors.getFieldErrors().stream()
					.map(ConciseFieldError::new)
					.collect(Collectors.toList());

			ObjectError globalError = errors.getGlobalError();
			populatePageModelForRegistration(pageModel, false, conciseFieldErrors,
					globalError == null ? "Some required information is missing or incorrect" : globalError.getDefaultMessage());

			return pageModel;
		}

		try
		{
			registrationService.generateRegistration(userRegistrationFormObject.getFirstName(),
					userRegistrationFormObject.getLastName(),
					userRegistrationFormObject.getEmailAddress());

			model.put("registrationEmail", userRegistrationFormObject.getEmailAddress());

			populatePageModelForRegistration(pageModel, true, null, null);
		}
		catch (Exception e)
		{
			populatePageModelForRegistration(pageModel, false, null, "Failed to register new user");
		}

		return pageModel;
	}

	@RequestMapping(path = "/confirm/{uuid}", method = RequestMethod.GET)
	public String confirmRegistration(@PathVariable UUID uuid, ModelMap model, HttpSession httpSession)
	{
		return doSignUpResponse(registrationService::confirmRegistration, "Your account is enabled and ready to log in to!", uuid, model, httpSession);
	}

	@RequestMapping(path = "/rescind/{uuid}", method = RequestMethod.GET)
	public String rescindRegistration(@PathVariable UUID uuid, ModelMap model, HttpSession httpSession)
	{
		return doSignUpResponse(registrationService::rescindRegistration, "All trace of your details have been deleted from our database...", uuid, model, httpSession);
	}

	private String doSignUpResponse(Consumer<UUID> serviceMethod, String registrationMessage, UUID uuid, ModelMap model, HttpSession httpSession)
	{
		model.remove("registrationEmail");
		httpSession.removeAttribute("registrationEmail");

		model.put("form", new UserRegistrationFormObject());
		model.put("registrationServiceAvailable", registrationService.isServiceAvailable());

		try
		{
			serviceMethod.accept(uuid);
			model.put("registrationResponseProcessed", true);
			model.put("registrationResponseMessage", registrationMessage);
		}
		catch (Exception e)
		{
			LOGGER.error("Registration confirmation failed", e);
			model.put("registrationResponseProcessed", false);
			model.put("registrationResponseMessage", "Ah... Something went wrong. We'll look in to it.");
		}

		return VIEW_NAME;
	}

	private static void populatePageModelForRegistration(Map<String, Object> pageModel, boolean isSuccessful, List<ConciseFieldError> conciseFieldErrors, String generalErrorMessage)
	{
		pageModel.put("success", isSuccessful);
		pageModel.put("error", generalErrorMessage);
		pageModel.put("fieldErrors", conciseFieldErrors);
	}
}