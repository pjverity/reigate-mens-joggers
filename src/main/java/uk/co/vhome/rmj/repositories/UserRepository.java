package uk.co.vhome.rmj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.co.vhome.rmj.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>
{
}