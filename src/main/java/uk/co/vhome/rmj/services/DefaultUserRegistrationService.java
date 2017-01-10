package uk.co.vhome.rmj.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * Handles the process of registering new users by storing new user entities in a
 * database and sending an e-mail notification to confirm the registration
 */
@Service
public class DefaultUserRegistrationService implements UserRegistrationService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final MailService mailService;

	private final UserDetailsManager userDetailsManager;

	private boolean serviceAvailable = false;

	@Inject
	public DefaultUserRegistrationService(MailService mailService, UserDetailsManager userDetailsManager)
	{
		this.mailService = mailService;
		this.userDetailsManager = userDetailsManager;

		// This service is only usable if it can mail registration confirmations
		serviceAvailable = this.mailService.isServiceAvailable();
	}

	/*
	 * Preconditions: Registration form validation must check that the service is valid, that the
	 * email address is not already in use, and that all parameters are sane.
	 */
	@Override
	public Set<String> generateRegistration(String firstName, String lastName, String emailAddress)
	{
		try
		{
			String generatedPassword = "temp";

			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("MEMBER");
			User user = new User(emailAddress,
					BCrypt.hashpw(generatedPassword, BCrypt.gensalt()),
					false,
					true,
					true,
					true,
					Collections.singleton(authority));

			userDetailsManager.createUser(user);

			mailService.sendRegistrationMail(emailAddress, UUID.randomUUID(), generatedPassword);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to register user", e);
			return Collections.singleton("There was a problem during registration");
		}

		return Collections.emptySet();
	}

	@Override
	public boolean isEmailAddressInUse(String emailAddress)
	{
		return userDetailsManager.userExists(emailAddress);
	}

	@Override
	public boolean isServiceAvailable()
	{
		return serviceAvailable;
	}
}