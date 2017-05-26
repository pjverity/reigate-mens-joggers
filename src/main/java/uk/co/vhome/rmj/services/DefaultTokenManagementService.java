package uk.co.vhome.rmj.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.notifications.BalanceUpdatedNotification;
import uk.co.vhome.rmj.notifications.LowBalanceNotification;
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

	private final EntityManager entityManager;

	private final PurchaseRepository purchaseRepository;

	private final UserAccountManagementService userAccountManagementService;

	private final NotificationService notificationService;

	@Inject
	public DefaultTokenManagementService(PurchaseRepository purchaseRepository, UserAccountManagementService userAccountManagementService, EntityManager entityManager, NotificationService notificationService)
	{
		this.purchaseRepository = purchaseRepository;
		this.userAccountManagementService = userAccountManagementService;
		this.entityManager = entityManager;
		this.notificationService = notificationService;
	}

	@Override
	public Purchase creditAccount(Long userId, int quantity)
	{
		assert quantity != 0;

		return doTransaction(userId, quantity);
	}

	@Override
	public Purchase debitAccount(Long userId, int quantity)
	{
		assert quantity != 0;

		return doTransaction(userId, -quantity);
	}

	@Override
	public Integer balanceForMember(Long userId)
	{
		Integer balance = purchaseRepository.calculateBalanceForUser(userId);

		return balance != null ? balance : 0;
	}

	private Purchase doTransaction(Long userId, int quantity)
	{
		if (!isUserEnabled(userId))
		{
			return null;
		}

		Purchase purchase = new Purchase(userId, quantity);
		purchaseRepository.save(purchase);

		Integer newBalance = balanceForMember(userId);

		// TODO - Derive action to perform from user preferences
		UserDetailsEntity userDetails = userAccountManagementService.findUserDetails(userId);
		if (quantity < 0 && newBalance <= 3)
		{
			notificationService.postNotification(new LowBalanceNotification(userDetails, quantity, newBalance));
		}
		else
		{
			notificationService.postNotification(new BalanceUpdatedNotification(userDetails, quantity, newBalance));
		}

		LOGGER.info("Purchase complete: {}", purchase);

		return purchase;
	}

	@Override
	public List<MemberBalance> balancesForAllEnabledMembers()
	{
		return ((List<MemberBalance>) entityManager.createNamedQuery(MemberBalance.ALL_ENABLED_MEMBERS_BALANCE_QUERY).getResultList());
	}

	private boolean isUserEnabled(Long userId)
	{
		UserDetailsEntity userDetails = userAccountManagementService.findUserDetails(userId);

		return userDetails != null && userDetails.isEnabled();
	}

}
