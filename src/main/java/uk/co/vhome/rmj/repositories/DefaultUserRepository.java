package uk.co.vhome.rmj.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import uk.co.vhome.rmj.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

@Repository
public class DefaultUserRepository implements UserRepository
{
	private static final Logger LOGGER = LogManager.getLogger();

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Collection<User> getAll()
	{
		Collection<User> users = entityManager.createNativeQuery("SELECT * FROM USERS", User.class).getResultList();

		LOGGER.info("Users {}", users);

		return users;
	}
}