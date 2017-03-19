package uk.co.vhome.rmj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.co.vhome.rmj.entities.Purchase;

/**
 * Repository interface for managing user tokens
 */
public interface PurchaseRepository extends JpaRepository<Purchase, Long>
{
	@Query("select sum(p.quantity) from Purchase p where p.username = ?1")
	Long calculateBalanceForUser(String username);
}
