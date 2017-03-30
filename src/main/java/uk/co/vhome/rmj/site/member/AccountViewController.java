package uk.co.vhome.rmj.site.member;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.co.vhome.rmj.services.UserAccountManagementService;

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

	private final UserAccountManagementService userAccountManagementService;

	@Inject
	public AccountViewController(UserAccountManagementService userAccountManagementService)
	{
		this.userAccountManagementService = userAccountManagementService;
	}

	@GetMapping("/member/account")
	public void get()
	{
	}

	@PostMapping("/member/account")
	public void changePassword(@AuthenticationPrincipal Principal principal,
	                           @Valid PasswordChangeFormObject formObject,
	                           BindingResult errors)
	{
		if (errors.hasErrors())
		{
			LOGGER.info("Password change failed: {}", errors);
			return;
		}

		try
		{
			userAccountManagementService.changePassword(principal.getName(), formObject.getOldPassword(), formObject.getNewPassword());

			formObject.setPasswordChanged(true);
		}
		catch (Exception e)
		{
			LOGGER.error("Changed password failed", e);

			errors.reject("error", "There was a problem changing the password. Please try again.");
		}
	}

	@SuppressWarnings("unused")
	@ModelAttribute
	PasswordChangeFormObject passwordChangeFormObject()
	{
		return new PasswordChangeFormObject();
	}

}
