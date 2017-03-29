package uk.co.vhome.rmj.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.repositories.PurchaseRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Default implementation for the {@link TokenManagementService}
 */
@Service
@Validated
public class DefaultTokenManagementService implements TokenManagementService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private int balanceUpperLimit;

	private int balanceLowerLimit;

	private int creditLimit;

	private int debitLimit;

	private final EntityManager entityManager;

	private final PurchaseRepository purchaseRepository;

	private final UserAccountManagementService userAccountManagementService;

	@Inject
	public DefaultTokenManagementService(PurchaseRepository purchaseRepository, UserAccountManagementService userAccountManagementService, EntityManager entityManager)
	{
		this.purchaseRepository = purchaseRepository;
		this.userAccountManagementService = userAccountManagementService;
		this.entityManager = entityManager;

		setBalanceLowerLimit(-5);
		setBalanceUpperLimit(20);

		setCreditLimit(10);
		setDebitLimit(1);
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

	public int getCreditLimit()
	{
		return creditLimit;
	}

	public void setCreditLimit(int creditLimit)
	{
		this.creditLimit = creditLimit;
	}

	public int getDebitLimit()
	{
		return debitLimit;
	}

	public void setDebitLimit(int debitLimit)
	{
		this.debitLimit = debitLimit;
	}

	@Override
	public Purchase creditAccount(String username, int quantity)
	{
		assert quantity > 0;

		if ( !creditWithLimit(quantity) || !creditWithinBalanceLimit(username, quantity) )
		{
			return null;
		}

		return doTransaction(username, quantity);
	}

	@Override
	public Purchase debitAccount(String username, int quantity)
	{
		assert quantity > 0;

		if ( !debitWithLimit(quantity) || !debitWithinBalanceLimit(username, quantity) )
		{
			return null;
		}

		return doTransaction(username, -quantity);
	}

	@Override
	public Integer balanceForMember(String username)
	{
		Integer balance = purchaseRepository.calculateBalanceForUser(username);

		return balance != null ? balance : 0;
	}

	private Purchase doTransaction(String username, int quantity)
	{
		if (!isUserEnabled(username))
		{
			return null;
		}

		Purchase purchase = new Purchase(username, quantity);
		purchaseRepository.save(purchase);

		LOGGER.info("Purchase complete: {}", purchase);

		return purchase;
	}

	@Override
	public List<MemberBalance> balanceForAllEnabledMembers()
	{
		return ((List<MemberBalance>) entityManager.createNamedQuery(MemberBalance.ALL_ENABLED_MEMBERS_BALANCE_QUERY).getResultList());
	}

	private boolean isUserEnabled(String username)
	{
		UserDetailsEntity userDetails = userAccountManagementService.findUserDetails(username);

		return userDetails != null && userDetails.isEnabled();
	}

	private boolean creditWithinBalanceLimit(String username, int quantity)
	{
		Integer currentBalance = balanceForMember(username);

		return currentBalance + quantity <= getBalanceUpperLimit();
	}

	private boolean debitWithinBalanceLimit(String username, int quantity)
	{
		Integer currentBalance = balanceForMember(username);

		return currentBalance - quantity >= getBalanceLowerLimit();
	}

	private boolean creditWithLimit(int quantity)
	{
		return quantity <= creditLimit;
	}

	private boolean debitWithLimit(int quantity)
	{
		return quantity <= debitLimit;
	}
}
