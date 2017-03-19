package uk.co.vhome.rmj.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.repositories.PurchaseRepository;

import javax.inject.Inject;

/**
 * Default implementation for the {@link TokenManagementService}
 */
@Service
public class DefaultTokenManagementService implements TokenManagementService
{
	private int balanceUpperLimit;

	private int balanceLowerLimit;

	private int purchaseLimit;

	private final PurchaseRepository purchaseRepository;

	private final UserAccountManagementService userAccountManagementService;

	@Inject
	public DefaultTokenManagementService(PurchaseRepository purchaseRepository, UserAccountManagementService userAccountManagementService)
	{
		this.purchaseRepository = purchaseRepository;
		this.userAccountManagementService = userAccountManagementService;
	}

	public int getBalanceUpperLimit()
	{
		return balanceUpperLimit;
	}

	public void setBalanceUpperLimit(int balanceUpperLimit)
	{
		this.balanceUpperLimit = balanceUpperLimit;
	}

	public int getBalanceLowerLimit()
	{
		return balanceLowerLimit;
	}

	public void setBalanceLowerLimit(int balanceLowerLimit)
	{
		this.balanceLowerLimit = balanceLowerLimit;
	}

	public int getPurchaseLimit()
	{
		return purchaseLimit;
	}

	public void setPurchaseLimit(int purchaseLimit)
	{
		this.purchaseLimit = purchaseLimit;
	}

	@Override
	public Purchase purchaseTokens(String username, int tokens)
	{
		if (!requestedTokensValid(tokens))
		{
			return null;
		}

		if (!balanceBelowUpperLimit(username, tokens))
		{
			return null;
		}

		if (!enabledUser(username))
		{
			return null;
		}

		Purchase purchase = new Purchase(username, tokens);
		purchaseRepository.save(purchase);
		return purchase;
	}

	@Override
	public Purchase deductToken(String username)
	{
		if (!balanceAboveLowerLimit(username, -1))
		{
			return null;
		}

		if (!enabledUser(username))
		{
			return null;
		}

		Purchase purchase = new Purchase(username, -1);
		purchaseRepository.save(purchase);
		return purchase;
	}

	private boolean enabledUser(String username)
	{
		UserDetails userDetails = userAccountManagementService.findUserDetails(username);

		return userDetails != null && userDetails.isEnabled();
	}

	private boolean balanceBelowUpperLimit(String username, int tokens)
	{
		Long balance = purchaseRepository.calculateBalanceForUser(username);

		return balance + tokens <= getBalanceUpperLimit();
	}

	private boolean balanceAboveLowerLimit(String username, int tokens)
	{
		Long balance = purchaseRepository.calculateBalanceForUser(username);

		return balance + tokens >= getBalanceLowerLimit();
	}

	@Override
	public Long tokenBalance(String userId)
	{
		return purchaseRepository.calculateBalanceForUser(userId);
	}

	private boolean requestedTokensValid(int tokens)
	{
		return tokens > 0 && tokens <= purchaseLimit;
	}
}
