package uk.co.vhome.rmj.site.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.vhome.rmj.entities.SupplementalUserDetails;
import uk.co.vhome.rmj.repositories.SupplementalUserDetailsRepository;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Secured administration functions
 */
@Controller
@RequestMapping("admin/usermanagement")
@SuppressWarnings("unused")
public class UserManagementController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String QUERY_USER_GROUP = "SELECT u.username, g.group_name FROM users u, groups g, group_members gm WHERE u.username = gm.username AND gm.group_id = g.id";

	private final SupplementalUserDetailsRepository supplementalUserDetailsRepository;

	private final JdbcUserDetailsManager userDetailsManager;

	@Inject
	public UserManagementController(SupplementalUserDetailsRepository supplementalUserDetailsRepository,
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

		List<UserGroup> userGroups = userDetailsManager
				                             .getJdbcTemplate()
				                             .query(QUERY_USER_GROUP,
				                                    new Object[]{},
				                                    new RowMapper<UserGroup>()
				                                    {
					                                    public UserGroup mapRow(ResultSet rs, int rowNum)
							                                    throws SQLException
					                                    {
						                                    return new UserGroup(rs.getString(1),
						                                                         rs.getString(2));
					                                    }
				                                    });


		UserManagementForm form = new UserManagementForm();

		userGroups.forEach(user ->
		                   {
			                   UserDetails userDetails = userDetailsManager.loadUserByUsername(user.username);
			                   SupplementalUserDetails supplementalUserDetails = supplementalUserDetailsRepository.findByEmailAddress(user.username);
			                   form.addUserSettings(userDetails.getUsername(), new MutableUser(userDetails, user.group_name));
			                   modelMap.put(userDetails.getUsername(), supplementalUserDetails);
		                   });

		return form;
	}

	private class UserGroup
	{
		String username;
		String group_name;

		UserGroup(String username, String group_name)
		{
			this.username = username;
			this.group_name = StringUtils.capitalize(group_name.toLowerCase());
		}
	}
}
