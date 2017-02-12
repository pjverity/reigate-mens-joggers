package uk.co.vhome.rmj.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
public interface SupplementalUserDetailsRepository extends CrudRepository<SupplementalUserDetails, Long>
{
	SupplementalUserDetails findByEmailAddress(String emailAddress);

	List<SupplementalUserDetails> findByEmailAddressIn(Collection<String> usersInGroup);

	@Modifying
	@Query("update SupplementalUserDetails u set u.lastLogin = :loginTime where u.emailAddress = :userId")
	void updateLastLoginFor(@Param("loginTime") LocalDateTime loginTime, @Param("userId") String userId);
}
