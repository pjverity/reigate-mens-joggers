package uk.co.vhome.rmj.services;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.security.Role;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Interface for maintaining member tokens
 */
public interface TokenManagementService
{
	@Secured({Role.ADMIN})
	Purchase creditAccount(String username, @Min(value = 1, message = "{validation.constraint.Min.creditBalance}") int quantity);

	@Secured({Role.ORGANISER})
	Purchase debitAccount(String username, @Min(value = 1, message = "{validation.constraint.Min.debitBalance}") int quantity);

	@Secured({Role.MEMBER})
	Integer balanceForMember(String username);

	@Secured({Role.ORGANISER})
	List<MemberBalance> balancesForAllEnabledMembers();
}
