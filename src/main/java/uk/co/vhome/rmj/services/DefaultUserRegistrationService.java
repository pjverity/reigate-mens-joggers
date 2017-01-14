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
import uk.co.vhome.rmj.repositories.RegistrationsRepository;

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
	private final MailService mailService;

	private final JdbcUserDetailsManager userDetailsManager;

	private final EntityManagerFactory entityManagerFactory;

	private final RegistrationsRepository registrationsRepository;

	private boolean serviceAvailable = false;

	@Inject
	public DefaultUserRegistrationService(MailService mailService, JdbcUserDetailsManager userDetailsManager, EntityManagerFactory entityManagerFactory, RegistrationsRepository registrationsRepository)
	{
		this.mailService = mailService;
		this.userDetailsManager = userDetailsManager;
		this.entityManagerFactory = entityManagerFactory;
		this.registrationsRepository = registrationsRepository;

		// This service is only usable if it can mail registration confirmations
		serviceAvailable = this.mailService.isServiceAvailable();
	}

	/*
	 * Preconditions: Registration form validation must check that the service is valid, that the
	 * email address is not already in use, and that all parameters are sane.
	 */
	@Override
	@Transactional(timeout = 15)
	public void generateRegistration(String firstName, String lastName, String emailAddress)
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
		Registration registration = registrationsRepository.save(new Registration(user.getUsername()));

		mailService.sendRegistrationMail(emailAddress, registration.getUuid(), generatedPassword);
	}

	@Override
	@Transactional(timeout = 15)
	public void confirmRegistration(UUID uuid)
	{
		Registration registration = registrationsRepository.findOne(uuid);

		if (registration == null)
		{
			throw new RuntimeException("No registration for uuid " + uuid + " exists");
		}

		UserDetails userDetails = userDetailsManager.loadUserByUsername(registration.getUsername());
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
	public void rescindRegistration(UUID uuid)
	{
		Registration registration = registrationsRepository.findOne(uuid);

		if (registration == null)
		{
			throw new RuntimeException("No registration for uuid " + uuid + " exists");
		}

		// http://stackoverflow.com/questions/41645928/cant-delete-from-two-tables-with-a-relation-in-an-transaction/41648562#41648562
		registrationsRepository.delete(uuid);
		EntityManagerHolder entityManager = (EntityManagerHolder) TransactionSynchronizationManager.getResource(entityManagerFactory);
		entityManager.getEntityManager().flush();

		userDetailsManager.deleteUser(registration.getUsername());
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