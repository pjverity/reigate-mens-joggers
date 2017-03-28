package uk.co.vhome.rmj.site.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.services.UserAccountManagementService;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	private final SessionRegistry sessionRegistry;

	@Inject
	public UserManagementController(UserAccountManagementService userAccountManagementService, SessionRegistry sessionRegistry)
	{
		this.userAccountManagementService = userAccountManagementService;
		this.sessionRegistry = sessionRegistry;
	}

	@RequestMapping(method = RequestMethod.POST)
	String updateModel(UserManagementForm userManagementForm)
	{
		userManagementForm.getUserManagementFormRows().forEach(r -> userAccountManagementService.setUserEnabled(r.getId(), r.isEnabled()));

		return "redirect:/admin/usermanagement";
	}

	@RequestMapping(method = RequestMethod.GET)
	UserManagementForm getView(ModelMap modelMap)
	{
		Map<String, Boolean> activeSessions = new HashMap<>();

		sessionRegistry.getAllPrincipals()
				.forEach(p -> sessionRegistry.getAllSessions(p, false)
						              .forEach(si -> activeSessions.put(((UserDetails) si.getPrincipal()).getUsername(), true)));

		modelMap.put("activeSessions", activeSessions);

		List<UserDetailsEntity> allUserDetails = userAccountManagementService.findAllUserDetails();
		List<UserDetailsEntity> sorted = allUserDetails.stream()
				                                 .sorted(Comparator.comparing(UserDetailsEntity::getLastName, String::compareToIgnoreCase)
						                                         .thenComparing(UserDetailsEntity::getFirstName, String::compareToIgnoreCase))
				                                 .collect(Collectors.toList());
		modelMap.put("userDetails", sorted);

		UserManagementForm userManagementForm = new UserManagementForm();

		List<UserManagementFormRow> formRows = sorted.stream()
				                                       .map(UserManagementFormRow::new)
				                                       .collect(Collectors.toList());

		userManagementForm.setUserManagementFormRows(formRows);

		return userManagementForm;
	}

}
