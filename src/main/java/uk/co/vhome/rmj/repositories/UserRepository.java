package uk.co.vhome.rmj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.co.vhome.rmj.entities.User;

import java.util.List;


/**
 * Basic repository for accessing/modifing user credentials in the database
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long>
{
	User findByUsername(String username);

	List<User> findAllByOrderByUserDetailLastNameAscUserDetailFirstNameAsc();
}
