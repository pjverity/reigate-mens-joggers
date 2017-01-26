package uk.co.vhome.rmj.services;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.entities.UserDetail;
import uk.co.vhome.rmj.repositories.UserDetailsRepository;

import javax.inject.Inject;
import java.util.Collections;

/**
 * Handles the process of registering new users by storing new user entities in a
 * database and sending an e-mail notification to confirm the registration
 */
@Service
public class DefaultUserRegistrationService implements UserRegistrationService
{
	private final MailService mailService;

	private final JdbcUserDetailsManager userDetailsManager;

	private final UserDetailsRepository userDetailsRepository;

	private boolean serviceAvailable = false;

	@Inject
	public DefaultUserRegistrationService(MailService mailService, JdbcUserDetailsManager userDetailsManager, UserDetailsRepository userDetailsRepository)
	{
		this.mailService = mailService;
		this.userDetailsManager = userDetailsManager;
		this.userDetailsRepository = userDetailsRepository;

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
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("MEMBER");
		User user = new User(userId.toLowerCase(),
				BCrypt.hashpw(password, BCrypt.gensalt()),
				true,
				true,
				true,
				true,
				Collections.singleton(authority));

		userDetailsManager.createUser(user);
		userDetailsRepository.save(new UserDetail(user.getUsername(), firstName, lastName));

		mailService.sendRegistrationMail(user.getUsername(), firstName);
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
}