package uk.co.vhome.clubbed.web.controllers.userregistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.co.vhome.clubbed.entities.UserEntity;
import uk.co.vhome.clubbed.security.AuthenticatedUser;
import uk.co.vhome.clubbed.usermanagement.Group;
import uk.co.vhome.clubbed.usermanagement.UserAccountManagementService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static uk.co.vhome.clubbed.security.SecurityConfiguration.*;

@Controller
public class UserRegistrationController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);

	private static final String MESSAGE_CODE_VALIDATION_CONSTRAINT_USER_REGISTRATION_VALID = "validation.constraint.UserRegistrationValid";

	private final MessageSource messageSource;

	private final UserAccountManagementService userAccountManagementService;

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

	@Autowired
	public UserRegistrationController(UserAccountManagementService userAccountManagementService, MessageSource messageSource)
	{
		this.userAccountManagementService = userAccountManagementService;
		this.messageSource = messageSource;
	}

	@ResponseBody
	@RequestMapping(path = "/registerNewUser", method = RequestMethod.POST)
	public Map<String,Object> registerNewMember(@ModelAttribute("form") @Valid UserRegistrationFormObject userRegistrationFormObject,
	                                  BindingResult errors, HttpServletRequest httpServletRequest) throws IOException
	{

		Map<String,Object> model = new HashMap<>();

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
			}
			else
			{
				message = globalError.getDefaultMessage();
			}

			populatePageModelForRegistration(model, false, conciseFieldErrors, message);

			return model;
		}

		try
		{
			UserEntity userEntity = AuthenticatedUser.callAsSystemUser( () -> userAccountManagementService.createUser(userRegistrationFormObject.getEmailAddress(),
			                                                                                                          userRegistrationFormObject.getFirstName(),
			                                                                                                          userRegistrationFormObject.getLastName(),
			                                                                                                          userRegistrationFormObject.getPassword(),
			                                                                                                          Group.MEMBER));

			// This appears to by-pass the SecurityConfiguration.authenticationSuccessHandler() handler, so have to duplicate
			// setting the session variables here
			httpServletRequest.login(userEntity.getUsername(), userRegistrationFormObject.getPassword());
			HttpSession httpSession = httpServletRequest.getSession();
			httpSession.setAttribute(USER_ID_SESSION_ATTRIBUTE, userEntity.getId());
			httpSession.setAttribute(USER_FIRST_NAME_SESSION_ATTRIBUTE, userEntity.getUserDetailsEntity().getFirstName());
			httpSession.setAttribute(USER_LAST_NAME_SESSION_ATTRIBUTE, userEntity.getUserDetailsEntity().getLastName());

			populatePageModelForRegistration(model, true, null, null);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to register user", e);
			populatePageModelForRegistration(model, false, null, "Failed to register new user");
		}

		return model;
	}

	private static void populatePageModelForRegistration(Map<String, Object> pageModel, boolean isSuccessful, List<ConciseFieldError> conciseFieldErrors, String generalErrorMessage)
	{
		pageModel.put("success", isSuccessful);
		pageModel.put("error", generalErrorMessage);
		pageModel.put("fieldErrors", conciseFieldErrors);
	}

}
