package uk.co.vhome.rmj.services;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.repositories.PurchaseRepository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static uk.co.vhome.rmj.UserConfigurations.*;

public class DefaultTokenManagementServiceTest
{
	private DefaultTokenManagementService tokenManagementService;

	@Mock
	private PurchaseRepository purchaseRepository;

	@Mock
	private UserAccountManagementService userAccountManagementService;

	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);

		tokenManagementService = new DefaultTokenManagementService(purchaseRepository, userAccountManagementService);
		tokenManagementService.setBalanceLowerLimit(-5);
		tokenManagementService.setBalanceUpperLimit(20);
		tokenManagementService.setPurchaseLimit(10);
	}

	@Test
	public void purchaseFailsWithInvalidTokenCount()
	{
		Purchase purchase = tokenManagementService.purchaseTokens(USER_ID, 0);
		assertNull(USER_ID + " should not be able to purchase 0 tokens", purchase);

		purchase = tokenManagementService.purchaseTokens(USER_ID, -1);
		assertNull(USER_ID + " should not be able to purchase <0 tokens", purchase);

		verifyZeroInteractions(purchaseRepository);
	}

	@Test
	public void purchaseFailsWhenPurchaseLimitExceeded()
	{
		Purchase purchase = tokenManagementService.purchaseTokens(USER_ID, 11);
		assertNull(USER_ID + " should not be able to purchase >10 tokens", purchase);

		verifyZeroInteractions(purchaseRepository);
	}

	@Test
	public void purchaseFailsWhenBalanceLimitExceeded()
	{
		when(purchaseRepository.calculateBalanceForUser(USER_ID)).thenReturn(16L);

		Purchase purchase = tokenManagementService.purchaseTokens(USER_ID, 5);
		assertNull(USER_ID + " should not be able to purchase tokens that would exceed balance limit", purchase);

		verify(purchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	public void purchaseFailsForInvalidUser()
	{
		// Unknown user
		when(userAccountManagementService.findUserDetails(USER_ID)).thenReturn(null);
		Purchase purchase = tokenManagementService.purchaseTokens(USER_ID, 5);
		assertNull( USER_ID + " should not be able to purchase tokens", purchase);

		// Disabled user
		when(userAccountManagementService.findUserDetails(USER_ID)).thenReturn(DISABLED_USER);
		purchase = tokenManagementService.purchaseTokens(USER_ID, 5);
		assertNull(USER_ID + " should not be able to purchase tokens", purchase);

		verify(purchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	public void purchaseSucceedsWithValidTokenCount()
	{
		when(userAccountManagementService.findUserDetails(USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.purchaseTokens(USER_ID, 1);
		assertNotNull(USER_ID + " should be able to purchase 1 token", purchase);

		assertEquals(1, purchase.getQuantity());

		verify(purchaseRepository).save(purchase);
	}

	@Test
	public void deductTokenForDisabledUserFails()
	{
		when(userAccountManagementService.findUserDetails(USER_ID)).thenReturn(DISABLED_USER);

		Purchase purchase = tokenManagementService.deductToken(USER_ID);
		assertNull( USER_ID + " should not be able to use tokens", purchase);

		verify(purchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	public void deductTokenForUserBelowBalanceThresholdFails()
	{
		when(userAccountManagementService.findUserDetails(USER_ID)).thenReturn(ENABLED_USER);
		when(purchaseRepository.calculateBalanceForUser(USER_ID)).thenReturn(-5L);

		Purchase purchase = tokenManagementService.deductToken(USER_ID);
		assertNull( USER_ID + " should not be able to use tokens", purchase);

		verify(purchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	public void deductTokenForUserSucceeds()
	{
		when(userAccountManagementService.findUserDetails(USER_ID)).thenReturn(ENABLED_USER);
		when(purchaseRepository.calculateBalanceForUser(USER_ID)).thenReturn(1L);

		Purchase purchase = tokenManagementService.deductToken(USER_ID);

		assertNotNull(USER_ID + " should be able to use 1 token", purchase);
		assertEquals(USER_ID + " should have had 1 token deducted", -1, purchase.getQuantity());

		verify(purchaseRepository).save(purchase);
	}

}