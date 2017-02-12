package uk.co.vhome.rmj.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uk.co.vhome.rmj.Group;
import uk.co.vhome.rmj.Role;
import uk.co.vhome.rmj.RunAs;
import uk.co.vhome.rmj.entities.SupplementalUserDetails;
import uk.co.vhome.rmj.repositories.SupplementalUserDetailsRepository;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Default implementation of the {@link UserAccountManagementService} service interface
 */
@Service
public class DefaultUserAccountManagementService implements UserAccountManagementService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String FIND_ALL_USER_INFO_SQL = "SELECT u.username, ud.first_name, ud.last_name, g.group_name, u.enabled, ud.last_login FROM" +
			                                                     " users u, groups g, group_members gm, user_details ud WHERE" +
			                                                     " u.username = gm.username AND" +
			                                                     " u.username = ud.username AND" +
			                                                     " gm.group_id = g.id" +
			                                                     " ORDER BY ud.last_name, ud.first_name";

	private final MailService mailService;

	private final JdbcUserDetailsManager userDetailsManager;

	private final SupplementalUserDetailsRepository supplementalUserDetailsRepository;

	private boolean serviceAvailable = false;

	@Value("${site.default.administrator.userId}")
	private String defaultAdminUserId;

	@Value("${site.default.administrator.firstName}")
	private String defaultAdminFirstName;

	@Value("${site.default.administrator.lastName}")
	private String defaultAdminLastName;

	@Value("${site.default.administrator.password}")
	private String defaultAdminPassword;

	@Inject
	public DefaultUserAccountManagementService(MailService mailService,
	                                           JdbcUserDetailsManager userDetailsManager,
	                                           SupplementalUserDetailsRepository supplementalUserDetailsRepository)
	{
		this.mailService = mailService;
		this.userDetailsManager = userDetailsManager;
		this.supplementalUserDetailsRepository = supplementalUserDetailsRepository;

		// This service is only usable if it can mail registration confirmations
		serviceAvailable = mailService.isServiceAvailable();
	}

	@Override
	@Transactional
	public void createBasicDefaultAccounts()
	{
		if (userDetailsManager.findAllGroups().isEmpty())
		{
			LOGGER.info("No group authorities found. Creating minimal accounts...");

			userDetailsManager.createGroup(Group.ADMIN, AuthorityUtils.createAuthorityList(Role.ADMIN, Role.MEMBER, Role.ORGANISER));
			userDetailsManager.createGroup(Group.ORGANISER, AuthorityUtils.createAuthorityList(Role.ORGANISER, Role.MEMBER));
			userDetailsManager.createGroup(Group.MEMBER, AuthorityUtils.createAuthorityList(Role.MEMBER));

			createUser(defaultAdminUserId, defaultAdminFirstName, defaultAdminLastName, defaultAdminPassword, Group.ADMIN);

			// Erase the password to be security conscious
			defaultAdminPassword = null;
		}
	}

	/*
	 * Preconditions: Registration form validation must check that the service is valid, that the
	 * email address is not already in use, and that all parameters are sane.
	 *
	 * In order to 'Run As' the system user to call the MailService interface, we have to have an
	 * authenticated user in the context, but as anyone can register, then use an 'Anonymous' user role
	 */
	@Override
	@Transactional(timeout = 15)
	@Secured({RunAs.SYSTEM, Role.ANON})
	public void registerNewUser(String userId, String firstName, String lastName, String password)
	{
		LOGGER.info("Registering new user {}", userId);

		createUser(userId, firstName, lastName, password, Group.MEMBER);

		SupplementalUserDetails supplementalUserDetails = supplementalUserDetailsRepository.findByEmailAddress(userId);

		mailService.sendRegistrationMail(supplementalUserDetails);

		sendAdministratorNotification(supplementalUserDetails);
	}

	@Override
	@Transactional(timeout = 15)
	public void changePassword(String userId, String oldPassword, String newPassword)
	{
		LOGGER.info("Changing password for user {}", userId);

		userDetailsManager.changePassword(oldPassword, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
	}

	@Override
	@Transactional(timeout = 15)
	public void createUser(String userId, String firstName, String lastName, String password, String groupName)
	{
		LOGGER.info("Creating new user {} in group {}", userId, groupName);

		// Use Spring's user details manager to create the user and associated authorities for now
		User user = new User(userId.toLowerCase(),
		                     BCrypt.hashpw(password, BCrypt.gensalt()),
		                     AuthorityUtils.NO_AUTHORITIES);

		userDetailsManager.createUser(user);

		userDetailsManager.addUserToGroup(userId, groupName);

		SupplementalUserDetails supplementalUserDetails = new SupplementalUserDetails(userId,
		                                                                              StringUtils.capitalize(firstName),
		                                                                              StringUtils.capitalize(lastName));
		supplementalUserDetailsRepository.save(supplementalUserDetails);
	}

	@Override
	public void setIsUserEnabled(String userId, boolean isEnabled)
	{
		LOGGER.info("Amending user {}. Enabled = {}", userId, isEnabled);

		UserDetails userDetails = userDetailsManager.loadUserByUsername(userId);

		User updatedUserDetails = new User(userDetails.getUsername(),
		                                   userDetails.getPassword(),
		                                   isEnabled,
		                                   userDetails.isAccountNonExpired(),
		                                   userDetails.isCredentialsNonExpired(),
		                                   userDetails.isAccountNonLocked(),
		                                   userDetails.getAuthorities());

		userDetailsManager.updateUser(updatedUserDetails);
	}

	@Override
	public List<UserAccountDetails> findAllUserDetails()
	{
		return userDetailsManager
				       .getJdbcTemplate()
				       .query(FIND_ALL_USER_INFO_SQL,
				              new Object[]{},
				              new RowMapper<UserAccountDetails>()
				              {
					              public UserAccountDetails mapRow(ResultSet rs, int rowNum)
							              throws SQLException
					              {
						              return new UserAccountDetails(rs.getString(1),
						                                            rs.getString(2),
						                                            rs.getString(3),
						                                            rs.getString(4),
						                                            rs.getBoolean(5),
						                                            rs.getTimestamp(6).toLocalDateTime());
					              }
				              });
	}

	@Override
	@Transactional
	public void updateLastLogin(String userId, long timestamp)
	{
		LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC);

		supplementalUserDetailsRepository.updateLastLoginFor(localDateTime, userId);
	}

	@Override
	public boolean isServiceAvailable()
	{
		return serviceAvailable;
	}

	private void sendAdministratorNotification(SupplementalUserDetails newUserDetails)
	{
		List<String> usersInGroup = userDetailsManager.findUsersInGroup(Group.ADMIN);
		List<SupplementalUserDetails> administrators = supplementalUserDetailsRepository.findByEmailAddressIn(usersInGroup);
		mailService.sendAdministratorNotification(administrators, newUserDetails);
	}
}