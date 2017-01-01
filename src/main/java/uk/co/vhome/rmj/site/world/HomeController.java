package uk.co.vhome.rmj.site.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * The sites home page that also provides user registration functions
 */
@Controller
@SessionAttributes({"registrationEmail"})
public class HomeController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String VIEW_NAME = "world/home";

	@RequestMapping(path = "/", method = RequestMethod.POST)
	@ResponseBody
	public List<FieldError> signup(@ModelAttribute("form") @Valid UserRegistrationFormObject userRegistrationFormObject,
	                               BindingResult errors, ModelMap model) throws IOException
	{
		if ( errors.hasErrors() )
		{
			LOGGER.error("Validation failed for user registration: {}", userRegistrationFormObject);
			return errors.getFieldErrors();
		}

		model.put("registrationEmail", userRegistrationFormObject.getEmailAddress());

		return Collections.emptyList();
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView get(ModelMap model)
	{
		model.put("form", new UserRegistrationFormObject());

		return new ModelAndView(VIEW_NAME, model);
	}
}