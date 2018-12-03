package uk.co.vhome.clubbed.web.site.member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import uk.co.vhome.clubbed.usermanagement.UserAccountManagementService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

import static uk.co.vhome.clubbed.security.SecurityConfiguration.USER_ID_SESSION_ATTRIBUTE;

/**
 * Controller for maintaining user account details
 */
@Controller
public class AccountViewController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountViewController.class);

	private final UserAccountManagementService userAccountManagementService;

	@Autowired
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

	@SuppressWarnings("unused")
	@ModelAttribute("tokenBalance")
	BigDecimal tokenBalance(@SessionAttribute(USER_ID_SESSION_ATTRIBUTE) Long userId)
	{
		return userAccountManagementService.findUser(userId).getUserDetailsEntity().getBalance();
	}

}
