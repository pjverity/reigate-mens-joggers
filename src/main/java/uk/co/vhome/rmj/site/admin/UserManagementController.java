package uk.co.vhome.rmj.site.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.vhome.rmj.security.Group;
import uk.co.vhome.rmj.services.UserAccountDetails;
import uk.co.vhome.rmj.services.UserAccountManagementService;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Secured administration functions
 */
@Controller
@RequestMapping("admin/user-management")
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
	String updateModel(UserManagementFormObject userManagementFormObject)
	{
		Map<String, UserAccountDetails> keyedOriginalDetails = new HashMap<>();

		userAccountManagementService.findAllUserDetails()
				.forEach(details -> keyedOriginalDetails.put(details.getEmailAddress(), details));

		userManagementFormObject.getUserAccountDetails().stream()
				.filter(userDetails -> isModifiedUser(userDetails, keyedOriginalDetails.get(userDetails.getEmailAddress())))
				.forEach(modifiedUser -> updateModifiedUser(modifiedUser, keyedOriginalDetails.get(modifiedUser.getEmailAddress())));

		return "redirect:/admin/user-management";
	}

	@RequestMapping(method = RequestMethod.GET)
	UserManagementFormObject getView(ModelMap modelMap)
	{
		UserManagementFormObject userManagementFormObject = new UserManagementFormObject();

		userManagementFormObject.setUserAccountDetails(userAccountManagementService.findAllUserDetails());

		modelMap.put("groups", Arrays.asList(Group.ADMIN,
		                                     Group.ORGANISER,
		                                     Group.MEMBER));

		return userManagementFormObject;
	}

	private boolean isModifiedUser(UserAccountDetails updatedDetails, UserAccountDetails originalUserDetails)
	{
		return updatedDetails.isEnabled() != originalUserDetails.isEnabled() ||
				       !updatedDetails.getGroup().equals(originalUserDetails.getGroup());
	}

	private void updateModifiedUser(UserAccountDetails updatedDetails, UserAccountDetails originalUserDetails)
	{
		userAccountManagementService.updateUser(originalUserDetails.getEmailAddress(),
		                                        updatedDetails.isEnabled(),
		                                        originalUserDetails.getGroup(),
		                                        updatedDetails.getGroup());
	}

}
