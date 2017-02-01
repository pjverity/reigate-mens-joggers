package uk.co.vhome.rmj.site.member;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import uk.co.vhome.rmj.entities.UserDetail;
import uk.co.vhome.rmj.repositories.UserRepository;
import uk.co.vhome.rmj.services.UserRegistrationService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.security.Principal;

/**
 * Controller for maintaining user account details
 */
@Controller
public class AccountViewController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final UserRepository userRepository;

	private final UserRegistrationService userRegistrationService;

	@Inject
	public AccountViewController(UserRepository userRepository, UserRegistrationService userRegistrationService)
	{
		this.userRepository = userRepository;
		this.userRegistrationService = userRegistrationService;
	}

	@RequestMapping(value = "/member/account", method = RequestMethod.GET)
	public void get(@RequestParam(value = "pwChanged", required = false) Boolean pwChanged, ModelMap modelMap)
	{
		modelMap.put("pwChanged", pwChanged);
		modelMap.put("formObject", new PasswordChangeFormObject());
	}

	@ModelAttribute("userDetail")
	public UserDetail userDetail(@AuthenticationPrincipal Principal principal)
	{
		return userRepository.findByUsername(principal.getName()).getUserDetail();
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public Object changePassword(@AuthenticationPrincipal Principal principal,
	                             @ModelAttribute("formObject") @Valid PasswordChangeFormObject passwords,
	                             BindingResult errors, ModelMap modelMap)
	{

		if ( errors.hasErrors() )
		{
			LOGGER.info("Password change failed");
			return "member/account";
		}

		try
		{
			userRegistrationService.changePassword(principal.getName(), passwords.getOldPassword(), passwords.getNewPassword());
		}
		catch (Exception e)
		{
			LOGGER.error("Changed password failed", e);
			errors.reject("error", "There was a problem changing the password. Please try again.");
			return "member/account";
		}

		modelMap.put("pwChanged", true);
		return new RedirectView("/member/account", true, false, true);
	}
}
