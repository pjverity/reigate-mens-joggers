package uk.co.vhome.rmj.site.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.vhome.rmj.entities.SupplementalUserDetails;
import uk.co.vhome.rmj.repositories.SupplementalUserDetailsRepository;

import javax.inject.Inject;
import java.util.List;

/**
 * Secured administration functions
 */
@Controller
@RequestMapping("admin/usermanagement")
@SuppressWarnings("unused")
public class AdminUserManagementController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final SupplementalUserDetailsRepository supplementalUserDetailsRepository;

	private final JdbcUserDetailsManager userDetailsManager;

	@Inject
	public AdminUserManagementController(SupplementalUserDetailsRepository supplementalUserDetailsRepository,
	                                     JdbcUserDetailsManager userDetailsManager)
	{
		this.supplementalUserDetailsRepository = supplementalUserDetailsRepository;
		this.userDetailsManager = userDetailsManager;
	}

	@RequestMapping(method = RequestMethod.POST)
	String updateModel(UserManagementForm userManagementForm)
	{
		userManagementForm.getUserSettings().forEach((k, v) ->
		                                             {
			                                             UserDetails userDetails = userDetailsManager.loadUserByUsername(k);
			                                             UserDetails updatedDetails = v.mergeWith(userDetails);
			                                             userDetailsManager.updateUser(updatedDetails);
		                                             });

		return "redirect:/admin/usermanagement";
	}

	@RequestMapping(method = RequestMethod.GET)
	UserManagementForm getView(ModelMap modelMap)
	{
		UserManagementForm userManagementForm = new UserManagementForm();

		List<String> users = userDetailsManager.getJdbcTemplate().queryForList("select username from users", String.class);

		UserManagementForm form = new UserManagementForm();

		users.forEach(user ->
		              {
			              UserDetails userDetails = userDetailsManager.loadUserByUsername(user);
			              SupplementalUserDetails supplementalUserDetails = supplementalUserDetailsRepository.findByEmailAddress(user);
			              form.addUserSettings(userDetails.getUsername(), new MutableUser(userDetails));
			              modelMap.put(userDetails.getUsername(), supplementalUserDetails);
		              });

		return form;
	}

}
