package uk.co.vhome.rmj.site.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import uk.co.vhome.rmj.repositories.UserRepository;

import javax.inject.Inject;

/**
 * Secured administration functions
 */
@Controller
@RequestMapping("admin/usermanagement")
@SuppressWarnings("unused")
@SessionAttributes("userManagementModel")
public class AdminUserManagementController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final UserRepository userRepository;

	@Inject
	public AdminUserManagementController(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	@RequestMapping(method = RequestMethod.POST)
	String updateModel(UserManagementModel userManagementModel)
	{
		LOGGER.debug(userManagementModel);

		userRepository.save(userManagementModel.getUsers());

		return "redirect:/admin/usermanagement";
	}

	@RequestMapping(method = RequestMethod.GET)
	UserManagementModel getView() {
		UserManagementModel userManagementModel = new UserManagementModel();
		userManagementModel.setUsers(userRepository.findAll());
		return userManagementModel;
	}

}
