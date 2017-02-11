package uk.co.vhome.rmj.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
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
import java.util.List;

/**
 * Handles the process of registering new users by storing new user entities in a
 * database and sending an e-mail notification to confirm the registration
 */
@Service
public class DefaultUserRegistrationService implements UserRegistrationService
{
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
	public DefaultUserRegistrationService(MailService mailService,
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
	public void initialiseFreshDB()
	{
		if (userDetailsManager.findAllGroups().isEmpty())
		{
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
	 * authenticated user in the context, but as anyone can register, then all an 'Anonymous' user role
	 */
	@Override
	@Transactional(timeout = 15)
	@Secured({RunAs.SYSTEM, Role.ANON})
	public void registerNewUser(String userId, String firstName, String lastName, String password)
	{
		createUser(userId, firstName, lastName, password, Group.MEMBER);

		SupplementalUserDetails supplementalUserDetails = supplementalUserDetailsRepository.findByEmailAddress(userId);

		mailService.sendRegistrationMail(supplementalUserDetails);

		sendAdministratorNotification(supplementalUserDetails);
	}

	@Override
	@Transactional(timeout = 15)
	public void changePassword(String userId, String oldPassword, String newPassword)
	{
		userDetailsManager.changePassword(oldPassword, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
	}

	@Override
	@Transactional(timeout = 15)
	public void createUser(String userId, String firstName, String lastName, String password, String groupName)
	{
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