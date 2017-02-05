package uk.co.vhome.rmj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.co.vhome.rmj.entities.SupplementalUserDetails;

import java.util.List;


/**
 * Basic repository for accessing/modifing user credentials in the database
 */
@Repository
public interface SupplementalUserDetailsRepository extends CrudRepository<SupplementalUserDetails, Long>
{
	SupplementalUserDetails findByEmailAddress(String emailAddress);

	List<SupplementalUserDetails> findAllByOrderByLastNameAscFirstNameAsc();
}
