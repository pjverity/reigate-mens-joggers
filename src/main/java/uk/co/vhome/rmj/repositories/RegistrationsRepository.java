package uk.co.vhome.rmj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.co.vhome.rmj.entities.Registration;

import java.util.UUID;

/**
 * Repository for maintaining {@link Registration} entities to deal with new user
 * registrations
 */
@Repository
public interface RegistrationsRepository extends CrudRepository<Registration, UUID>
{
}
