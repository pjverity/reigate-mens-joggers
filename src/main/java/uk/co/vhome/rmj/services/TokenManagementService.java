package uk.co.vhome.rmj.services;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.security.Role;

import java.util.List;

/**
 * Interface for maintaining member tokens
 */
public interface TokenManagementService
{
	@Secured({Role.ORGANISER})
	Purchase adjustBalance(String username, int quantity);

	@Secured({Role.MEMBER})
	Integer balanceForMember(String username);

	@Secured({Role.ORGANISER})
	List<MemberBalance> balanceForAllEnabledMembers();
}
