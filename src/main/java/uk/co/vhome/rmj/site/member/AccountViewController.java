package uk.co.vhome.rmj.site.member;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import uk.co.vhome.rmj.services.core.TokenManagementService;
import uk.co.vhome.rmj.services.core.UserAccountManagementService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.security.Principal;

import static uk.co.vhome.rmj.config.ServletContextConfiguration.USER_ID_SESSION_ATTRIBUTE;

/**
 * Controller for maintaining user account details
 */
@Controller
public class AccountViewController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final UserAccountManagementService userAccountManagementService;

	private final TokenManagementService tokenManagementService;

	@Inject
	public AccountViewController(UserAccountManagementService userAccountManagementService,
	                             TokenManagementService tokenManagementService)
	{
		this.userAccountManagementService = userAccountManagementService;
		this.tokenManagementService = tokenManagementService;
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

	@SuppressWarnings("unused")
	@ModelAttribute("tokenBalance")
	Integer tokenBalance(@AuthenticationPrincipal Principal principal, @SessionAttribute(USER_ID_SESSION_ATTRIBUTE) Long userId)
	{
		return tokenManagementService.balanceForMember(userId);
	}

}
