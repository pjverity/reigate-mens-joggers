package uk.co.vhome.rmj.site.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.vhome.rmj.services.UserAccountManagementService;

import javax.inject.Inject;

/**
 * Secured administration functions
 */
@Controller
@RequestMapping("admin/usermanagement")
@SuppressWarnings("unused")
public class UserManagementController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final UserAccountManagementService userAccountManagementService;

	@Inject
	public UserManagementController(UserAccountManagementService userAccountManagementService)
	{
		this.userAccountManagementService = userAccountManagementService;
	}

	@RequestMapping(method = RequestMethod.POST)
	String updateModel(UserManagementForm userManagementForm)
	{
		userManagementForm.getUserAccountDetails()
				.forEach(d -> userAccountManagementService.setIsUserEnabled(d.getEmailAddress(), d.isEnabled()));

		return "redirect:/admin/usermanagement";
	}

	@RequestMapping(method = RequestMethod.GET)
	UserManagementForm getView()
	{
		UserManagementForm userManagementForm = new UserManagementForm();

		userManagementForm.setUserAccountDetails(userAccountManagementService.findAllUserDetails());

		return userManagementForm;
	}

}
