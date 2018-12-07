package uk.co.vhome.clubbed.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.vhome.clubbed.entities.Order;

/**
 * Spring Data repository for accessing {@link Order} entities
 */
public interface OrderRepository extends JpaRepository<Order, Long>
{
}
