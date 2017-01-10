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
import org.springframework.web.servlet.ModelAndView;
import uk.co.vhome.rmj.services.UserRegistrationService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

	@RequestMapping(path = "/", method = RequestMethod.POST)
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
			populatePageModel(pageModel, false, conciseFieldErrors,
					globalError == null ? "Some required information is missing or incorrect" : globalError.getDefaultMessage());

			return pageModel;
		}

		Set<String> registrationErrors = registrationService.generateRegistration(userRegistrationFormObject.getFirstName(),
				userRegistrationFormObject.getLastName(),
				userRegistrationFormObject.getEmailAddress());

		if (registrationErrors.isEmpty())
		{
			model.put("registrationEmail", userRegistrationFormObject.getEmailAddress());
			populatePageModel(pageModel, true, null, null);
		}
		else
		{
			populatePageModel(pageModel, false, null, String.join(",", registrationErrors));
		}

		return pageModel;
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView get(ModelMap model)
	{
		model.put("form", new UserRegistrationFormObject());
		model.put("registrationServiceAvailable", registrationService.isServiceAvailable());

		return new ModelAndView(VIEW_NAME, model);
	}

	private static void populatePageModel(Map<String, Object> pageModel, boolean isSuccessful, List<ConciseFieldError> conciseFieldErrors, String generalErrorMessage)
	{
		pageModel.put("success", isSuccessful);
		pageModel.put("error", generalErrorMessage);
		pageModel.put("fieldErrors", conciseFieldErrors);
	}
}