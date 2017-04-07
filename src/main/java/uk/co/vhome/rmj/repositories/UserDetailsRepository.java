package uk.co.vhome.rmj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.entities.UserDetailsEntity;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;


/**
 * Basic repository for accessing/modifying supplemental user information in the database
 */
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long>
{
	UserDetailsEntity findByUsername(String username);

	Set<UserDetailsEntity> findByUsernameIn(Collection<String> usernames);

	@Modifying
	@Query("update UserDetailsEntity u set u.lastLogin = :loginTime where u.username = :username")
	@Transactional
	void updateLastLogin(@Param("username") String username, @Param("loginTime") Instant loginTime);
}
