package uk.co.vhome.rmj.services.core;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.clubbed.security.Role;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.entities.Purchase;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Interface for maintaining member tokens
 */
public interface TokenManagementService
{
	@Secured({Role.ADMIN, Role.RUN_AS_NEW_USER})
	Purchase creditAccount(Long userId, @Min(value = 1, message = "{validation.constraint.Min.creditBalance}") int quantity);

	@Secured({Role.ORGANISER})
	Purchase debitAccount(Long userId, @Min(value = 1, message = "{validation.constraint.Min.debitBalance}") int quantity);

	@Secured({Role.MEMBER})
	Integer balanceForMember(Long userId);

	@Secured({Role.ORGANISER})
	List<MemberBalance> balancesForAllEnabledMembers();
}
