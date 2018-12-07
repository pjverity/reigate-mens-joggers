package uk.co.vhome.clubbed.usermanagement;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uk.co.vhome.clubbed.apiobjects.NewUserEvent;
import uk.co.vhome.clubbed.repositories.UserDetailsRepository;
import uk.co.vhome.clubbed.repositories.UsersRepository;
import uk.co.vhome.clubbed.InitialSiteUser;
import uk.co.vhome.clubbed.entities.UserDetailsEntity;
import uk.co.vhome.clubbed.entities.UserEntity;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

/**
 * Default implementation of the {@link UserAccountManagementService} service interface
 */
@Service
public class DefaultUserAccountManagementService implements UserAccountManagementService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUserAccountManagementService.class);

	private final JdbcUserDetailsManager userDetailsManager;

	private final UsersRepository usersRepository;

	private final UserDetailsRepository userDetailsRepository;

	private final SessionRegistry sessionRegistry;

	private final EventBus eventBus;

	private InitialSiteUser initialSiteUser;

	@Autowired
	public DefaultUserAccountManagementService(InitialSiteUser initialSiteUser,
	                                           JdbcUserDetailsManager userDetailsManager,
	                                           UsersRepository usersRepository,
	                                           UserDetailsRepository userDetailsRepository,
	                                           SessionRegistry sessionRegistry,
	                                           EventBus eventBus)
	{
		this.initialSiteUser = initialSiteUser;
		this.userDetailsManager = userDetailsManager;
		this.usersRepository = usersRepository;
		this.userDetailsRepository = userDetailsRepository;
		this.sessionRegistry = sessionRegistry;
		this.eventBus = eventBus;
	}

	@Override
	@Transactional
	public void createBasicDefaultAccounts()
	{
		Collection<Integer> enabledAdmins = usersRepository.enabledUsersInGroup(true, Group.ADMIN);

		if (enabledAdmins.isEmpty())
		{
			LOGGER.info("Creating initial admin account");

			createUser(initialSiteUser.getId(),
			           initialSiteUser.getFirstName(),
			           initialSiteUser.getLastName(),
			           initialSiteUser.getPassword(),
			           Group.ADMIN);

			// Erase the details to be security conscious. Should also remove the bean from the context?
			initialSiteUser = null;
		}
	}

	@Override
	public void changePassword(String username, String oldPassword, String newPassword)
	{
		LOGGER.info("Changing password for user {}", username);

		userDetailsManager.changePassword(oldPassword, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
	}

	@Override
	@Transactional(timeout = 15)
	public UserEntity createUser(String username, String firstName, String lastName, String password, String groupName)
	{
		LOGGER.info("Creating new user {} in group {}", username, groupName);

		// Use Spring's user details manager to create the user and associated authorities for now
		User user = new User(username.toLowerCase(),
		                     BCrypt.hashpw(password, BCrypt.gensalt()),
		                     AuthorityUtils.NO_AUTHORITIES);

		userDetailsManager.createUser(user);

		userDetailsManager.addUserToGroup(user.getUsername(), groupName);

		UserDetailsEntity userDetailsEntity = createUserDetails(user.getUsername(), firstName, lastName);

		EventMessage<NewUserEvent> eventMessage = asEventMessage(new NewUserEvent(userDetailsEntity.getUserEntity().getUsername(),
		                                                                          userDetailsEntity.getFirstName(),
		                                                                          userDetailsEntity.getLastName()));

		eventBus.publish(eventMessage);

		return userDetailsEntity.getUserEntity();
	}

	@Override
	public void setUserEnabled(Long id, boolean enable)
	{
		UserEntity user = findUser(id);

		if (user.isEnabled() == enable)
		{
			return;
		}

		user.setEnabled(enable);

		usersRepository.save(user);

		if (!enable)
		{
			invalidateSession(user.getUsername());
		}
	}

	@Override
	public List<UserEntity> findAllUsers()
	{
		return Collections.unmodifiableList(usersRepository.findAll());
	}

	@Override
	public UserEntity save(UserEntity userDetails)
	{
		return usersRepository.save(userDetails);
	}

	@Override
	public Collection<UserEntity> enabledUsersInGroup(boolean enabled, String group)
	{
		Collection<Long> userIds = usersRepository.enabledUsersInGroup(enabled, group).stream()
				                              .map(Integer::longValue)
				                              .collect(Collectors.toSet());


		return usersRepository.findAllById(userIds);
	}

	@Override
	public List<UserEntity> findUsers(Collection<Long> userIds)
	{
		return usersRepository.findAllById(userIds);
	}

	@Override
	public UserEntity findUser(Long userId)
	{
		return usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("Unable to find userId: " + userId));
	}

	@Override
	public UserEntity findUser(String username)
	{
		return usersRepository.findByUsername(username);
	}

	private Collection<UserEntity> findEnabledAdminDetails()
	{
		Collection<Long> enabledUsersInGroup = usersRepository.enabledUsersInGroup(true, Group.ADMIN)
				                                       .stream()
				                                       .mapToLong(Integer::longValue)
				                                       .boxed()
				                                       .collect(Collectors.toSet());

		return usersRepository.findAllById(enabledUsersInGroup);
	}

	@Override
	public void updateLastLogin(String username, long timestamp)
	{
		usersRepository.updateLastLogin(username, Instant.ofEpochMilli(timestamp));
	}

	private UserDetailsEntity createUserDetails(String username, String firstName, String lastName)
	{
		UserEntity userEntity = usersRepository.findByUsername(username);

		String updatedFirstName = StringUtils.capitalize(firstName);
		String updatedLastName = StringUtils.capitalize(lastName);

		UserDetailsEntity newDetailsEntity = new UserDetailsEntity(userEntity, updatedFirstName, updatedLastName);
		userEntity.setUserDetailsEntity(newDetailsEntity);

		userDetailsRepository.save(newDetailsEntity);

		return userDetailsRepository.save(newDetailsEntity);
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