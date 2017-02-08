package uk.co.vhome.rmj.services;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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

	@Inject
	public DefaultUserRegistrationService(MailService mailService,
	                                      JdbcUserDetailsManager userDetailsManager,
	                                      SupplementalUserDetailsRepository supplementalUserDetailsRepository)
	{
		this.mailService = mailService;
		this.userDetailsManager = userDetailsManager;
		this.supplementalUserDetailsRepository = supplementalUserDetailsRepository;

		// This service is only usable if it can mail registration confirmations
		serviceAvailable = this.mailService.isServiceAvailable();
	}

	/*
	 * Preconditions: Registration form validation must check that the service is valid, that the
	 * email address is not already in use, and that all parameters are sane.
	 */
	@Override
	@Transactional(timeout = 15)
	public void registerNewUser(String userId, String firstName, String lastName, String password)
	{
		// Use Spring's user details manager to create the user and associated authorities for now
		User user = new User(userId.toLowerCase(),
		                     BCrypt.hashpw(password, BCrypt.gensalt()),
		                     AuthorityUtils.NO_AUTHORITIES);

		userDetailsManager.createUser(user);

		userDetailsManager.addUserToGroup(userId, "MEMBER");

		SupplementalUserDetails supplementalUserDetails = new SupplementalUserDetails(userId,
		                                                                              StringUtils.capitalize(firstName),
		                                                                              StringUtils.capitalize(lastName));

		supplementalUserDetailsRepository.save(supplementalUserDetails);

		mailService.sendRegistrationMail(supplementalUserDetails);

		// This is done as a separate call so that it is not part of this transaction. It's not
		// critical if the site admin doesn't receive the e-mail, so don't roll back if this fails
		sendAdministratorNotification(supplementalUserDetails);
	}

	@Override
	@Transactional(timeout = 15)
	public void changePassword(String userId, String oldPassword, String newPassword)
	{
		userDetailsManager.changePassword(oldPassword, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
	}

	@Override
	public boolean isServiceAvailable()
	{
		return serviceAvailable;
	}

	private void sendAdministratorNotification(SupplementalUserDetails newUserDetails)
	{
		List<String> usersInGroup = userDetailsManager.findUsersInGroup("ADMIN");
		List<SupplementalUserDetails> administrators = supplementalUserDetailsRepository.findByEmailAddressIn(usersInGroup);
		mailService.sendAdministratorNotification(administrators, newUserDetails);
	}
}