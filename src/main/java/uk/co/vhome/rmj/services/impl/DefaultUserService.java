package uk.co.vhome.rmj.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.model.User;
import uk.co.vhome.rmj.repositories.UserRepository;
import uk.co.vhome.rmj.services.UserService;

import javax.inject.Inject;

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

	@Override
	@Transactional
	public Iterable<User> getAllUsers()
	{
		Iterable<User> users = userRepository.findAll();
		LOGGER.info("Got {}", users);
		return users;
	}

	@Override
	public boolean signUp(String emailAddress, String password)
	{
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

		User newUser = new User(emailAddress, hashedPassword);

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

}