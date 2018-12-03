package uk.co.vhome.clubbed.paymentmanagement;

import org.axonframework.eventhandling.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import uk.co.vhome.clubbed.apiobjects.BalanceUpdatedEvent;
import uk.co.vhome.clubbed.apiobjects.LowBalanceEvent;
import uk.co.vhome.clubbed.repositories.OrderRepository;
import uk.co.vhome.clubbed.entities.Order;
import uk.co.vhome.clubbed.entities.UserDetailsEntity;
import uk.co.vhome.clubbed.entities.UserEntity;
import uk.co.vhome.clubbed.usermanagement.UserAccountManagementService;

import java.math.BigDecimal;

import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

/**
 * Default implementation for the {@link TokenManagementService}
 */
@Service
@Validated
public class DefaultTokenManagementService implements TokenManagementService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTokenManagementService.class);

	private final OrderRepository orderRepository;

	private final UserAccountManagementService userAccountManagementService;

	private final EventBus eventBus;

	private static final BigDecimal DEFAULT_UNIT_COST = BigDecimal.ONE;

	@Autowired
	public DefaultTokenManagementService(OrderRepository orderRepository,
	                                     UserAccountManagementService userAccountManagementService,
	                                     EventBus eventBus)
	{
		this.orderRepository = orderRepository;
		this.userAccountManagementService = userAccountManagementService;
		this.eventBus = eventBus;
	}

	@Override
	@Transactional
	public Order creditAccount(Long userId, Long quantity)
	{
		assert quantity != 0;

		return doTransaction(userId, DEFAULT_UNIT_COST, quantity);
	}

	@Override
	@Transactional
	public Order debitAccount(Long userId, Long quantity)
	{
		assert quantity != 0;

		return doTransaction(userId, DEFAULT_UNIT_COST, -quantity);
	}

	private Order doTransaction(Long userId, BigDecimal unitCost, Long quantity)
	{
		if (!isUserEnabled(userId))
		{
			return null;
		}

		Order order = placeOrder(userId, unitCost, quantity);
		UserDetailsEntity userDetails = updateBalance(userId, unitCost, quantity);

		BigDecimal currentBalance = userDetails.getBalance();

		// TODO - Derive action to perform from user preferences
		if (quantity < 0 && currentBalance.compareTo(BigDecimal.valueOf(3)) <= 0)
		{
			eventBus.publish(asEventMessage(new LowBalanceEvent(userDetails.getUserEntity().getUsername(),
			                                                    userDetails.getFirstName(),
			                                                    userDetails.getLastName(),
			                                                    quantity,
			                                                    currentBalance)));
		}
		else
		{
			eventBus.publish(asEventMessage(new BalanceUpdatedEvent(userDetails.getUserEntity().getUsername(),
			                                                        userDetails.getFirstName(),
			                                                        quantity,
			                                                        currentBalance)));
		}

		LOGGER.info("Order complete: {}", order);

		return order;
	}

	private UserDetailsEntity updateBalance(Long userId, BigDecimal unitCost, Long quantity)
	{
		UserEntity userEntity = userAccountManagementService.findUser(userId);

		BigDecimal currentBalance = userEntity.getUserDetailsEntity().getBalance();

		BigDecimal totalCost = unitCost.multiply(BigDecimal.valueOf(quantity));

		userEntity.getUserDetailsEntity().setBalance(currentBalance.add(totalCost));

		return userAccountManagementService.save(userEntity).getUserDetailsEntity();
	}

	private Order placeOrder(Long userId, BigDecimal unitCost, Long quantity)
	{
		Order newOrder = new Order(userId, unitCost, quantity);
		return orderRepository.save(newOrder);
	}

	private boolean isUserEnabled(Long userId)
	{
		UserEntity userEntity = userAccountManagementService.findUser(userId);

		return userEntity.isEnabled();
	}

}
