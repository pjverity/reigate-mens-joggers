package uk.co.vhome.rmj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.co.vhome.rmj.entities.UserPrincipal;


/**
 * Basic repository for accessing/modifing user credentials in the database
 */
@Repository
public interface UserRepository extends CrudRepository<UserPrincipal, Long>
{
}
