package uk.co.vhome.rmj.services;

import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.entities.MemberBalance;

import java.util.List;

/**
 * Interface for maintaining member tokens
 */
public interface TokenManagementService
{
	Purchase modifyBalance(String username, int quantity);

	Integer balanceForMember(String username);

	List<MemberBalance> balanceForAllEnabledMembers();
}
