package uk.co.vhome.rmj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.co.vhome.rmj.entities.SupplementalUserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


/**
 * Basic repository for accessing/modifying supplemental user information in the database
 */
@Repository
public interface SupplementalUserDetailsRepository extends JpaRepository<SupplementalUserDetails, Long>
{
	SupplementalUserDetails findByUsername(String username);

	List<SupplementalUserDetails> findByUsernameIn(Collection<String> usersInGroup);

	@Modifying
	@Query("update SupplementalUserDetails u set u.lastLogin = :loginTime where u.username = :username")
	void updateLastLoginFor(@Param("loginTime") LocalDateTime loginTime, @Param("username") String username);
}
