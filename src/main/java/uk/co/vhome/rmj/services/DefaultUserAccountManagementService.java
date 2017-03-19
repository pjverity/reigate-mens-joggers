package uk.co.vhome.rmj.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uk.co.vhome.rmj.config.InitialSiteUser;
import uk.co.vhome.rmj.entities.SupplementalUserDetails;
import uk.co.vhome.rmj.repositories.SupplementalUserDetailsRepository;
import uk.co.vhome.rmj.security.Group;
import uk.co.vhome.rmj.security.Role;
import uk.co.vhome.rmj.security.RunAs;

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

	private static final String QUERY_ENABLED_ADMINS = "SELECT u.username FROM users u, group_members gm, groups g WHERE" +
			                                                                                     " u.enabled = TRUE AND" +
			                                                                                     " u.username = gm.username AND" +
			                                                                                     " gm.group_id = g.id AND" +
			                                                                                     " g.group_name = ?";

	private final MailService mailService;

	private final JdbcUserDetailsManager userDetailsManager;

	private final SupplementalUserDetailsRepository supplementalUserDetailsRepository;

	private final SessionRegistry sessionRegistry;

	private boolean serviceAvailable = false;

	private InitialSiteUser initialSiteUser;

	@Inject
	public DefaultUserAccountManagementService(InitialSiteUser initialSiteUser,
	                                           MailService mailService,
	                                           JdbcUserDetailsManager userDetailsManager,
	                                           SupplementalUserDetailsRepository supplementalUserDetailsRepository,
	                                           SessionRegistry sessionRegistry)
	{
		this.mailService = mailService;
		this.userDetailsManager = userDetailsManager;
		this.supplementalUserDetailsRepository = supplementalUserDetailsRepository;

		// This service is only usable if it can mail registration confirmations
		serviceAvailable = mailService.isServiceAvailable();
		this.sessionRegistry = sessionRegistry;

		this.initialSiteUser = initialSiteUser;
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

			createUser(initialSiteUser.getId(),
			           initialSiteUser.getFirstName(),
			           initialSiteUser.getLastName(),
			           initialSiteUser.getPassword(),
			           Group.ADMIN);

			// Erase the details to be security conscious. Should also remove the bean from the context?
			initialSiteUser = null;
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
	public void registerNewUser(String username, String firstName, String lastName, String password)
	{
		LOGGER.info("Registering new user {}", username);

		createUser(username, firstName, lastName, password, Group.MEMBER);

		SupplementalUserDetails supplementalUserDetails = supplementalUserDetailsRepository.findByUsername(username);

		mailService.sendRegistrationMail(supplementalUserDetails);

		sendAdministratorNotification(supplementalUserDetails);
	}

	@Override
	@Transactional(timeout = 15)
	public void changePassword(String username, String oldPassword, String newPassword)
	{
		LOGGER.info("Changing password for user {}", username);

		userDetailsManager.changePassword(oldPassword, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
	}

	@Override
	@Transactional(timeout = 15)
	public void createUser(String username, String firstName, String lastName, String password, String groupName)
	{
		LOGGER.info("Creating new user {} in group {}", username, groupName);

		// Use Spring's user details manager to create the user and associated authorities for now
		User user = new User(username.toLowerCase(),
		                     BCrypt.hashpw(password, BCrypt.gensalt()),
		                     AuthorityUtils.NO_AUTHORITIES);

		userDetailsManager.createUser(user);

		userDetailsManager.addUserToGroup(username, groupName);

		SupplementalUserDetails supplementalUserDetails = new SupplementalUserDetails(username,
		                                                                              StringUtils.capitalize(firstName),
		                                                                              StringUtils.capitalize(lastName));
		supplementalUserDetailsRepository.save(supplementalUserDetails);
	}

	@Override
	public void updateUser(String username, boolean isEnabled, String removeFromGroup, String addToGroup)
	{
		LOGGER.info("Amending user {}. Enabled = {}", username, isEnabled);

		UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

		User updatedUserDetails = new User(userDetails.getUsername(),
		                                   userDetails.getPassword(),
		                                   isEnabled,
		                                   userDetails.isAccountNonExpired(),
		                                   userDetails.isCredentialsNonExpired(),
		                                   userDetails.isAccountNonLocked(),
		                                   userDetails.getAuthorities());

		userDetailsManager.updateUser(updatedUserDetails);

		if ( !removeFromGroup.equals(addToGroup) )
		{
			userDetailsManager.removeUserFromGroup(username, removeFromGroup);
			userDetailsManager.addUserToGroup(username, addToGroup);
		}
		if (!isEnabled)
		{
			User user = new User(username, "", AuthorityUtils.NO_AUTHORITIES);
			List<SessionInformation> allSessions = sessionRegistry.getAllSessions(user, false);

			// Only expire the session, don't remove it otherwise the browser will resend the authenticated
			// session cookie and have access to the site when the account was disabled.
			// By forcing a new session to be created, authentication is re-evaluated
			allSessions.forEach(SessionInformation::expireNow);
		}

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
						              String emailAddress = rs.getString(1);

						              // See if the user has an active session
						              User user = new User(emailAddress, "", AuthorityUtils.NO_AUTHORITIES);
						              boolean hasActiveSession = !sessionRegistry.getAllSessions(user, false).isEmpty();

						              return new UserAccountDetails(emailAddress,
						                                            rs.getString(2),
						                                            rs.getString(3),
						                                            rs.getString(4),
						                                            rs.getBoolean(5),
						                                            rs.getTimestamp(6).toLocalDateTime(),
						                                            hasActiveSession);
					              }
				              });
	}

	@Override
	public UserDetails findUserDetails(String username)
	{
		try
		{
			return userDetailsManager.loadUserByUsername(username);
		}
		catch (UsernameNotFoundException e)
		{
			LOGGER.info("User not found: {}", username);
			return null;
		}
	}

	@Override
	@Transactional
	public void updateLastLogin(String username, long timestamp)
	{
		LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC);

		supplementalUserDetailsRepository.updateLastLoginFor(localDateTime, username);
	}

	@Override
	public boolean isServiceAvailable()
	{
		return serviceAvailable;
	}

	private void sendAdministratorNotification(SupplementalUserDetails newUserDetails)
	{
		// TODO - Create a proper ORM entity model and interfaces to run these queries rather than using JDBC queries
		List<String> enabledUsersInGroup = userDetailsManager.getJdbcTemplate().queryForList(QUERY_ENABLED_ADMINS,
		                                                                                     new String[]{Group.ADMIN},
		                                                                                     String.class);

		List<SupplementalUserDetails> administrators = supplementalUserDetailsRepository.findByUsernameIn(enabledUsersInGroup);
		mailService.sendAdministratorNotification(administrators, newUserDetails);
	}
}