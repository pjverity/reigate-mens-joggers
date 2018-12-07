package uk.co.vhome.clubbed.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.vhome.clubbed.entities.UserDetailsEntity;

/**
 * Spring Data repository for accessng {@link UserDetailsEntity} entities
 */
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long>
{
}
