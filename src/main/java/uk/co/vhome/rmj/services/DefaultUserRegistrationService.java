package uk.co.vhome.rmj.services;

import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import uk.co.vhome.rmj.entities.Registration;
import uk.co.vhome.rmj.entities.UserDetail;
import uk.co.vhome.rmj.repositories.RegistrationsRepository;
import uk.co.vhome.rmj.repositories.UserDetailsRepository;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.UUID;

/**
 * Handles the process of registering new users by storing new user entities in a
 * database and sending an e-mail notification to confirm the registration
 */
@Service
public class DefaultUserRegistrationService implements UserRegistrationService
{
	private static char chars[] = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
	                                         'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
	                                         '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@', '£', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', '#', '_', '?'};

	private final MailService mailService;

	private final JdbcUserDetailsManager userDetailsManager;

	private final EntityManagerFactory entityManagerFactory;

	private final RegistrationsRepository registrationsRepository;

	private final UserDetailsRepository userDetailsRepository;

	private boolean serviceAvailable = false;

	@Inject
	public DefaultUserRegistrationService(MailService mailService, JdbcUserDetailsManager userDetailsManager, EntityManagerFactory entityManagerFactory, RegistrationsRepository registrationsRepository, UserDetailsRepository userDetailsRepository)
	{
		this.mailService = mailService;
		this.userDetailsManager = userDetailsManager;
		this.entityManagerFactory = entityManagerFactory;
		this.registrationsRepository = registrationsRepository;
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
	public void generateRegistration(String userId, String firstName, String lastName)
	{
		String generatedPassword = generateRandomPassword();

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("MEMBER");
		User user = new User(userId,
				BCrypt.hashpw(generatedPassword, BCrypt.gensalt()),
				false,
				true,
				true,
				true,
				Collections.singleton(authority));

		userDetailsManager.createUser(user);
		Registration registration = registrationsRepository.save(new Registration(userId));
		userDetailsRepository.save(new UserDetail(userId, firstName, lastName));

		mailService.sendRegistrationMail(userId, firstName, registration.getUuid(), generatedPassword);
	}

	private String generateRandomPassword()
	{
		StringBuilder stringBuilder = new StringBuilder(10);

		for (int i = 0; i < 10; ++i )
		{
			char c = chars[(int)Math.round(Math.random() * (chars.length-1))];
			stringBuilder.append(c);
		}

		return stringBuilder.toString();
	}

	@Override
	@Transactional(timeout = 15)
	public void acceptRegistration(UUID uuid)
	{
		Registration registration = registrationsRepository.findOne(uuid);

		if (registration == null)
		{
			throw new RuntimeException("No registration for uuid " + uuid + " exists");
		}

		UserDetails userDetails = userDetailsManager.loadUserByUsername(registration.getUserId());
		User updatedUser = new User(userDetails.getUsername(),
				userDetails.getPassword(),
				true,
				true,
				true,
				true,
				userDetails.getAuthorities());

		userDetailsManager.updateUser(updatedUser);
		registrationsRepository.delete(uuid);
	}

	@Override
	@Transactional(timeout = 15)
	public void declineRegistration(UUID uuid)
	{
		Registration registration = registrationsRepository.findOne(uuid);

		if (registration == null)
		{
			throw new RuntimeException("No registration for uuid " + uuid + " exists");
		}

		// http://stackoverflow.com/questions/41645928/cant-delete-from-two-tables-with-a-relation-in-an-transaction/41648562#41648562
		registrationsRepository.delete(uuid);
		userDetailsRepository.delete(registration.getUserId());

		EntityManagerHolder entityManager = (EntityManagerHolder) TransactionSynchronizationManager.getResource(entityManagerFactory);
		entityManager.getEntityManager().flush();

		userDetailsManager.deleteUser(registration.getUserId());
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