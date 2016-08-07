package uk.co.vhome.rmj.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.model.User;
import uk.co.vhome.rmj.repositories.UserRepository;
import uk.co.vhome.rmj.services.UserService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collections;

@Service
class DefaultUserService implements UserService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final UserRepository userRepository;

	@Inject
	public DefaultUserService(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	@PostConstruct
	private void initialiseDatabase()
	{
		if ( userRepository.count() > 0 ) {
			return;
		}

		LOGGER.info("Initialising bare database...");

		adminSignUp();
	}

	@Override
	@Transactional
	public Iterable<User> getAllUsers()
	{
		Iterable<User> users = userRepository.findAll();
		LOGGER.info("Got {}", users);
		return users;
	}

	@Override
	public boolean memberSignUp(String emailAddress, String password, String firstName, String lastName)
	{
		return signUp(emailAddress, password, firstName, lastName, User.MEMBER);
	}

	private void adminSignUp()
	{
		signUp("admin@reigatemensjoggers.co.uk", "admin", "Administrator", "", User.ADMIN);
	}

	private boolean signUp(String emailAddress, String password, String firstName, String lastName, String role)
	{
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

		User newUser = new User(emailAddress, hashedPassword, firstName, lastName, role);

		try
		{
			userRepository.save(newUser);
		}
		catch (DataIntegrityViolationException e)
		{
			LOGGER.info("Failed to create user: {} ({})", emailAddress, e.getMessage());
			return false;
		}

		return true;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		User user = userRepository.findByEmailAddress(username);

		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().trim());

		return new org.springframework.security.core.userdetails.User(user.getEmailAddress(),
				user.getPassword(),
				user.isEnabled(),
				true,
				true,
				true,
				Collections.singletonList(grantedAuthority));
	}
}