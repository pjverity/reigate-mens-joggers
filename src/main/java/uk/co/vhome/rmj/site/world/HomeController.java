package uk.co.vhome.rmj.site.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import uk.co.vhome.rmj.entities.Registration;
import uk.co.vhome.rmj.entities.UserDetail;
import uk.co.vhome.rmj.repositories.RegistrationsRepository;
import uk.co.vhome.rmj.repositories.UserDetailsRepository;
import uk.co.vhome.rmj.services.UserRegistrationService;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
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

	private static final String MESSAGE_CODE_REGISTRATION_ACCEPTED_SUCCESS = "registration.accepted.success";
	private static final String MESSAGE_CODE_REGISTRATION_PROCESSING_FAILED = "registration.processing.failed";
	private static final String MESSAGE_CODE_REGISTRATION_DECLINED_SUCCESS = "registration.declined.success";
	private static final String MESSAGE_CODE_VALIDATION_CONSTRAINT_USER_REGISTRATION_VALID = "validation.constraint.UserRegistrationValid";

	public enum Action
	{
		ACCEPT("Activate Account"),
		DECLINE("Delete Account");

		private final String action;

		Action(String action)
		{
			this.action = action;
		}

		public String getAction()
		{
			return action;
		}
	}

	private final UserRegistrationService registrationService;

	private final UserDetailsRepository userDetailsRepository;

	private final RegistrationsRepository registrationsRepository;

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
	public HomeController(UserRegistrationService registrationService, UserDetailsRepository userDetailsRepository, RegistrationsRepository registrationsRepository, MessageSource messageSource)
	{
		this.registrationService = registrationService;
		this.userDetailsRepository = userDetailsRepository;
		this.registrationsRepository = registrationsRepository;
		this.messageSource = messageSource;
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String get(ModelMap model)
	{
		model.put("form", new UserRegistrationFormObject());
		model.put("registrationServiceAvailable", registrationService.isServiceAvailable());

		return VIEW_NAME;
	}

	@RequestMapping(path = "/registration/{action}/{uuid}", method = RequestMethod.GET)
	public String registrationAccept(@PathVariable UUID uuid,
	                                 @PathVariable @NotBlank String action,
	                                 ModelMap model)
	{
		model.put("form", new UserRegistrationFormObject());
		model.put("registrationServiceAvailable", registrationService.isServiceAvailable());

		try
		{
			Action a = Action.valueOf(action.toUpperCase());

			Registration registration = registrationsRepository.findOne(uuid);
			UserDetail userDetail = userDetailsRepository.findOne(registration.getUserId());

			model.put("firstName", userDetail.getFirstName());
			model.put("registrationAction", a);
			model.put("uuid", uuid);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to process users registration response", e);

			model.put("registrationResponseProcessed", false);
			model.put("registrationResponseMessage", "Oops, something went wrong. We're looking in to it.");
		}

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
			registrationService.generateRegistration(userRegistrationFormObject.getEmailAddress(),
					userRegistrationFormObject.getFirstName(),
					userRegistrationFormObject.getLastName()
			);

			model.put("registrationEmail", userRegistrationFormObject.getEmailAddress());

			populatePageModelForRegistration(pageModel, true, null, null);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to register user", e);
			populatePageModelForRegistration(pageModel, false, null, "Failed to register new user");
		}

		return pageModel;
	}

	@RequestMapping(path = "/registration/complete", method = RequestMethod.POST)
	public String registrationComplete(@RequestParam @NotBlank UUID uuid,
	                                   @RequestParam @NotBlank String action,
	                                   ModelMap model, HttpSession httpSession)
	{
		Action action1 = Action.valueOf(action);

		Consumer<UUID> serviceMethod = action1 == Action.ACCEPT ? registrationService::acceptRegistration : registrationService::declineRegistration;
		String message = messageSource.getMessage(action1 == Action.ACCEPT ? MESSAGE_CODE_REGISTRATION_ACCEPTED_SUCCESS : MESSAGE_CODE_REGISTRATION_DECLINED_SUCCESS, null, Locale.getDefault());

		return doSignUpResponse(serviceMethod, message, uuid, model, httpSession);
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

			String message = messageSource.getMessage(MESSAGE_CODE_REGISTRATION_PROCESSING_FAILED, null, Locale.getDefault());
			model.put("registrationResponseProcessed", false);
			model.put("registrationResponseMessage", message);
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