package uk.co.vhome.clubbed.web.controllers.usermanagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.vhome.clubbed.entities.UserDetailsEntity;
import uk.co.vhome.clubbed.entities.UserEntity;
import uk.co.vhome.clubbed.usermanagement.UserAccountManagementService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	private final SessionRegistry sessionRegistry;

	@Autowired
	public UserManagementController(UserAccountManagementService userAccountManagementService, SessionRegistry sessionRegistry)
	{
		this.userAccountManagementService = userAccountManagementService;
		this.sessionRegistry = sessionRegistry;
	}

	@RequestMapping(method = RequestMethod.POST)
	String updateModel(UserManagementFormObject userManagementForm)
	{
		userManagementForm.getUserManagementFormRows().forEach(r -> userAccountManagementService.setUserEnabled(r.getUserEntity().getId(),
		                                                                                                        r.getUserEntity().getUserEntity().isEnabled()));

		return "redirect:/admin/user-management";
	}

	@RequestMapping(method = RequestMethod.GET)
	UserManagementFormObject getView(ModelMap modelMap)
	{
		Map<String, Boolean> activeSessions = new HashMap<>();

		sessionRegistry.getAllPrincipals()
				.forEach(p -> sessionRegistry.getAllSessions(p, false)
						              .forEach(si -> activeSessions.put(((UserDetails) si.getPrincipal()).getUsername(), true)));

		modelMap.put("activeSessions", activeSessions);

		List<UserDetailsEntity> sorted = userAccountManagementService.findAllUsers()
				                                 .stream()
												 .map(UserEntity::getUserDetailsEntity)
				                                 .sorted(Comparator.comparing(UserDetailsEntity::getLastName, String::compareToIgnoreCase)
						                                         .thenComparing(UserDetailsEntity::getFirstName, String::compareToIgnoreCase))
				                                 .collect(Collectors.toList());
		modelMap.put("userDetails", sorted);

		UserManagementFormObject userManagementForm = new UserManagementFormObject();

		List<UserManagementFormRow> formRows = sorted.stream()
				                                       .map(UserManagementFormRow::new)
				                                       .collect(Collectors.toList());

		userManagementForm.setUserManagementFormRows(formRows);

		return userManagementForm;
	}

}
