package uk.co.vhome.rmj.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uk.co.vhome.rmj.config.InitialSiteUser;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.repositories.UserDetailsRepository;
import uk.co.vhome.rmj.security.Group;
import uk.co.vhome.rmj.security.Role;
import uk.co.vhome.rmj.security.RunAs;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

/**
 * Default implementation of the {@link UserAccountManagementService} service interface
 */
@Service
public class DefaultUserAccountManagementService implements UserAccountManagementService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String QUERY_ENABLED_ADMINS = "SELECT u.username FROM users u, group_members gm, groups g WHERE" +
			                                                                                     " u.enabled = TRUE AND" +
			                                                                                     " u.username = gm.username AND" +
			                                                                                     " gm.group_id = g.id AND" +
			                                                                                     " g.group_name = ?";

	private final MailService mailService;

	private final JdbcUserDetailsManager userDetailsManager;

	private final UserDetailsRepository userDetailsRepository;

	private final SessionRegistry sessionRegistry;

	private InitialSiteUser initialSiteUser;

	@Inject
	public DefaultUserAccountManagementService(InitialSiteUser initialSiteUser,
	                                           MailService mailService,
	                                           JdbcUserDetailsManager userDetailsManager,
	                                           UserDetailsRepository userDetailsRepository,
	                                           SessionRegistry sessionRegistry)
	{
		this.mailService = mailService;
		this.userDetailsManager = userDetailsManager;
		this.userDetailsRepository = userDetailsRepository;

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

		UserDetailsEntity userDetailsEntity = userDetailsRepository.findByUsername(username);

		mailService.sendRegistrationMail(userDetailsEntity);

		// TODO - Don't put this in the same transaction. If the notification sends to the
		// user Ok and fails to sent to the admin, then whole thing rolls back leaving the
		// user wondering why they can't log in to their account!
		sendAdministratorNotification(userDetailsEntity);
	}

	@Override
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

		updateUserDetails(username, firstName, lastName);
	}

	@Override
	public void setUserEnabled(Long id, boolean enable)
	{
		UserDetailsEntity user = userDetailsRepository.findOne(id);

		if ( user.isEnabled() == enable )
		{
			return;
		}

		user.setEnabled(enable);

		userDetailsRepository.save(user);

		if ( !enable )
		{
			invalidateSession(user.getUsername());
		}
	}

	@Override
	public List<UserDetailsEntity> findAllUserDetails()
	{
		return userDetailsRepository.findAll();
	}


	@Override
	public UserDetailsEntity findUserDetails(String username)
	{
		return userDetailsRepository.findByUsername(username);
	}

	@Override
	public void updateLastLogin(String username, long timestamp)
	{
		userDetailsRepository.updateLastLogin(username, Instant.ofEpochMilli(timestamp));
	}

	private void updateUserDetails(String username, String firstName, String lastName)
	{
		UserDetailsEntity userDetailsEntity = userDetailsRepository.findByUsername(username);

		userDetailsEntity.setFirstName(StringUtils.capitalize(firstName));
		userDetailsEntity.setLastName(StringUtils.capitalize(lastName));

		userDetailsRepository.save(userDetailsEntity);
	}

	private void sendAdministratorNotification(UserDetailsEntity newUserDetails)
	{
		// TODO - Create a proper ORM entity model and interfaces to run these queries rather than using JDBC queries
		List<String> enabledUsersInGroup = userDetailsManager.getJdbcTemplate().queryForList(QUERY_ENABLED_ADMINS,
		                                                                                     new String[]{Group.ADMIN},
		                                                                                     String.class);

		List<UserDetailsEntity> administrators = userDetailsRepository.findByUsernameIn(enabledUsersInGroup);
		mailService.sendAdministratorNotification(administrators, newUserDetails);
	}

	private void invalidateSession(String username)
	{
		User user = new User(username, "", AuthorityUtils.NO_AUTHORITIES);
		List<SessionInformation> allSessions = sessionRegistry.getAllSessions(user, false);

		// Only expire the session, don't remove it otherwise the browser will resend the authenticated
		// session cookie and have access to the site when the account was disabled.
		// By forcing a new session to be created, authentication is re-evaluated
		allSessions.forEach(SessionInformation::expireNow);
	}
}