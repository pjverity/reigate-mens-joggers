package uk.co.vhome.clubbed.paymentmanagement;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.clubbed.entities.Order;
import uk.co.vhome.clubbed.usermanagement.Role;

import javax.validation.constraints.Min;

/**
 * Interface for maintaining member tokens
 */
public interface TokenManagementService
{
	@Secured({Role.ADMIN})
	Order creditAccount(Long userId, @Min(value = 1, message = "{validation.constraint.Min.creditBalance}") Long quantity);

	@Secured({Role.ORGANISER})
	Order debitAccount(Long userId, @Min(value = 1, message = "{validation.constraint.Min.debitBalance}") Long quantity);
}
