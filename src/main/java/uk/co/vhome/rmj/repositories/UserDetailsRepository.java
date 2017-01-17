package uk.co.vhome.rmj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.co.vhome.rmj.entities.UserDetail;

/**
 * Repository for persisting and querying for with {@link UserDetail} entities
 */
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetail, String>
{
}
