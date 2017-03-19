package uk.co.vhome.rmj.services;

import uk.co.vhome.rmj.entities.Purchase;

/**
 * Interface for maintaining member tokens
 */
public interface TokenManagementService
{
	Purchase purchaseTokens(String userId, int tokens);

	Purchase deductToken(String username);

	Long tokenBalance(String userId);
}
