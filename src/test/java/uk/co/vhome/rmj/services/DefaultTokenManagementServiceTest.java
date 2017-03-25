package uk.co.vhome.rmj.services;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.repositories.PurchaseRepository;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static uk.co.vhome.rmj.UserConfigurations.*;

public class DefaultTokenManagementServiceTest
{
	@Mock
	private EntityManager mockEntityManager;

	@Mock
	private PurchaseRepository mockPurchaseRepository;

	@Mock
	private UserAccountManagementService mockUserAccountManagementService;

	private DefaultTokenManagementService tokenManagementService;

	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);

		tokenManagementService = new DefaultTokenManagementService(mockPurchaseRepository, mockUserAccountManagementService, mockEntityManager);
		tokenManagementService.setBalanceLowerLimit(-5);
		tokenManagementService.setBalanceUpperLimit(20);
		tokenManagementService.setPurchaseLimit(10);
	}

	@Test
	public void purchaseFailsWithInvalidTokenCount()
	{
		Purchase purchase = tokenManagementService.adjustBalance(ENABLED_USER_ID, 0);
		assertNull(ENABLED_USER_ID + " should not be able to purchase 0 tokens", purchase);

		verifyZeroInteractions(mockPurchaseRepository);
	}

	@Test
	public void purchaseFailsWhenPurchaseLimitExceeded()
	{
		Purchase purchase = tokenManagementService.adjustBalance(ENABLED_USER_ID, 11);
		assertNull(ENABLED_USER_ID + " should not be able to purchase >10 tokens", purchase);

		verifyZeroInteractions(mockPurchaseRepository);
	}

	@Test
	public void purchaseFailsWhenBalanceLimitExceeded()
	{
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(16);

		Purchase purchase = tokenManagementService.adjustBalance(ENABLED_USER_ID, 5);
		assertNull(ENABLED_USER_ID + " should not be able to purchase tokens that would exceed balance limit", purchase);

		verify(mockPurchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	public void purchaseFailsForInvalidUser()
	{
		// Unknown user
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(null);
		Purchase purchase = tokenManagementService.adjustBalance(ENABLED_USER_ID, 5);
		assertNull(ENABLED_USER_ID + " should not be able to purchase tokens", purchase);

		// Disabled user
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(DISABLED_USER);
		purchase = tokenManagementService.adjustBalance(ENABLED_USER_ID, 5);
		assertNull(ENABLED_USER_ID + " should not be able to purchase tokens", purchase);

		verify(mockPurchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	public void purchaseSucceedsWithValidTokenCount()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.adjustBalance(ENABLED_USER_ID, 1);
		assertNotNull(ENABLED_USER_ID + " should be able to purchase 1 token", purchase);

		assertEquals(1, purchase.getQuantity());

		verify(mockPurchaseRepository).save(purchase);
	}

	@Test
	public void deductTokenForDisabledUserFails()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(DISABLED_USER);

		Purchase purchase = tokenManagementService.adjustBalance(ENABLED_USER_ID, -1);
		assertNull(ENABLED_USER_ID + " should not be able to use tokens", purchase);

		verify(mockPurchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	public void deductTokenForUserBelowBalanceThresholdFails()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(-5);

		Purchase purchase = tokenManagementService.adjustBalance(ENABLED_USER_ID, -1);
		assertNull(ENABLED_USER_ID + " should not be able to use tokens", purchase);

		verify(mockPurchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	public void deductTokenForUserSucceeds()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(1);

		Purchase purchase = tokenManagementService.adjustBalance(ENABLED_USER_ID, -1);

		assertNotNull(ENABLED_USER_ID + " should be able to use 1 token", purchase);
		assertEquals(ENABLED_USER_ID + " should have had 1 token deducted", -1, purchase.getQuantity());

		verify(mockPurchaseRepository).save(purchase);
	}

}