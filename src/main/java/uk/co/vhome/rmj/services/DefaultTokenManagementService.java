package uk.co.vhome.rmj.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.repositories.PurchaseRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Default implementation for the {@link TokenManagementService}
 */
@Service
public class DefaultTokenManagementService implements TokenManagementService
{
	private int balanceUpperLimit;

	private int balanceLowerLimit;

	private int purchaseLimit;

	private final EntityManager entityManager;

	private final PurchaseRepository purchaseRepository;

	private final UserAccountManagementService userAccountManagementService;

	@Inject
	public DefaultTokenManagementService(PurchaseRepository purchaseRepository, UserAccountManagementService userAccountManagementService, EntityManager entityManager)
	{
		this.purchaseRepository = purchaseRepository;
		this.userAccountManagementService = userAccountManagementService;
		this.entityManager = entityManager;
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
	public Purchase modifyBalance(String username, int quantity)
	{
		if ( quantity > 0 )
		{
			if ( !requestedQuantityValid(quantity) || !balanceBelowUpperLimit(username, quantity) )
			{
				return null;
			}
		}
		else if ( quantity < 0 )
		{
			if ( !balanceAboveLowerLimit(username, quantity) )
			{
				return null;
			}
		}
		else
		{
			return null;
		}

		if (!isUserEnabled(username))
		{
			return null;
		}

		Purchase purchase = new Purchase(username, quantity);
		purchaseRepository.save(purchase);
		return purchase;
	}

	@Override
	public Integer balanceForMember(String username)
	{
		return purchaseRepository.calculateBalanceForUser(username);
	}

	@Override
	public List<MemberBalance> balanceForAllEnabledMembers()
	{
		return ((List<MemberBalance>) entityManager.createNamedQuery(MemberBalance.ALL_ENABLED_MEMBERS_BALANCE_QUERY).getResultList());
	}

	private boolean isUserEnabled(String username)
	{
		UserDetails userDetails = userAccountManagementService.findUserDetails(username);

		return userDetails != null && userDetails.isEnabled();
	}

	private boolean balanceBelowUpperLimit(String username, int quantity)
	{
		Integer balance = purchaseRepository.calculateBalanceForUser(username);

		return balance + quantity <= getBalanceUpperLimit();
	}

	private boolean balanceAboveLowerLimit(String username, int quantity)
	{
		Integer balance = purchaseRepository.calculateBalanceForUser(username);

		return balance + quantity >= getBalanceLowerLimit();
	}

	private boolean requestedQuantityValid(int quantity)
	{
		return quantity <= purchaseLimit;
	}
}
