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
	}

	@Test(expected = AssertionError.class)
	public void creditZeroTokensFails()
	{
		tokenManagementService.creditAccount(ENABLED_USER_ID, 0);

		verifyZeroInteractions(mockPurchaseRepository);
	}

	@Test(expected = AssertionError.class)
	public void debitZeroTokensFails()
	{
		tokenManagementService.debitAccount(ENABLED_USER_ID, 0);

		verifyZeroInteractions(mockPurchaseRepository);
	}

	@Test
	public void creditFailsWhenPurchaseLimitExceeded()
	{
		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 11);
		assertNull(ENABLED_USER_ID + " should not be able to purchase >10 tokens", purchase);

		verifyZeroInteractions(mockPurchaseRepository);
	}

	@Test
	public void creditFailsWhenBalanceLimitExceeded()
	{
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(16);

		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 5);
		assertNull(ENABLED_USER_ID + " should not be able to purchase tokens that would exceed balance limit", purchase);

		verify(mockPurchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	public void creditOrDebitFailsForInvalidUser()
	{
		Long randomUserId = 4L;

		// Unknown user - Credit
		when(mockUserAccountManagementService.findUserDetails(randomUserId)).thenReturn(null);
		Purchase purchase = tokenManagementService.creditAccount(randomUserId, 5);
		assertNull(randomUserId + " should not be able to purchase tokens", purchase);

		// Unknown user - Debit
		when(mockUserAccountManagementService.findUserDetails(randomUserId)).thenReturn(null);
		purchase = tokenManagementService.debitAccount(randomUserId, 1);
		assertNull(randomUserId + " should not be able to purchase tokens", purchase);

		// Disabled user - Credit
		when(mockUserAccountManagementService.findUserDetails(DISABLED_USER_ID)).thenReturn(DISABLED_USER);
		purchase = tokenManagementService.creditAccount(DISABLED_USER_ID, 5);
		assertNull(DISABLED_USER_ID + " should not be able to purchase tokens", purchase);

		// Disabled user - Debit
		when(mockUserAccountManagementService.findUserDetails(DISABLED_USER_ID)).thenReturn(DISABLED_USER);
		purchase = tokenManagementService.debitAccount(DISABLED_USER_ID, 1);
		assertNull(DISABLED_USER_ID + " should not be able to purchase tokens", purchase);

		verify(mockPurchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	public void creditSucceeds()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(5);

		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 1);
		assertNotNull(ENABLED_USER_ID + " should be able to purchase 1 token", purchase);

		assertEquals(1, purchase.getQuantity());

		verify(mockPurchaseRepository).save(purchase);
	}

	@Test
	public void creditSucceedsWithNoPreviousBalance()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(null);

		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 1);
		assertNotNull(ENABLED_USER_ID + " should be able to purchase 1 token", purchase);

		assertEquals(1, purchase.getQuantity());

		verify(mockPurchaseRepository).save(purchase);
	}

	@Test
	public void debitSucceeds()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(5);

		Purchase purchase = tokenManagementService.debitAccount(ENABLED_USER_ID, 1);
		assertNotNull(ENABLED_USER_ID + " should be able to spend 1 token", purchase);

		assertEquals(-1, purchase.getQuantity());

		verify(mockPurchaseRepository).save(purchase);
	}

}