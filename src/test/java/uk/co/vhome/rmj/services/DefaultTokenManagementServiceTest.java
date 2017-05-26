package uk.co.vhome.rmj.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.repositories.PurchaseRepository;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static uk.co.vhome.rmj.UserConfigurations.*;

class DefaultTokenManagementServiceTest
{
	@Mock
	private EntityManager mockEntityManager;

	@Mock
	private PurchaseRepository mockPurchaseRepository;

	@Mock
	private UserAccountManagementService mockUserAccountManagementService;

	@Mock
	private NotificationService notificationService;

	private DefaultTokenManagementService tokenManagementService;

	@BeforeEach
	void init()
	{
		MockitoAnnotations.initMocks(this);

		tokenManagementService = new DefaultTokenManagementService(mockPurchaseRepository, mockUserAccountManagementService, mockEntityManager, notificationService);
	}

	@Test
	void creditZeroTokensFails()
	{
		assertThrows(AssertionError.class, () -> tokenManagementService.creditAccount(ENABLED_USER_ID, 0));

		verifyZeroInteractions(mockPurchaseRepository);
	}

	@Test
	void debitZeroTokensFails()
	{
		assertThrows(AssertionError.class, () -> tokenManagementService.debitAccount(ENABLED_USER_ID, 0));

		verifyZeroInteractions(mockPurchaseRepository);
	}

	@Test
	void creditFailsWhenPurchaseLimitExceeded()
	{
		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 11);
		assertNull(ENABLED_USER_ID + " should not be able to purchase >10 tokens", purchase);

		verifyZeroInteractions(mockPurchaseRepository);
	}

	@Test
	void creditFailsWhenBalanceLimitExceeded()
	{
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(16);

		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 5);
		assertNull(ENABLED_USER_ID + " should not be able to purchase tokens that would exceed balance limit", purchase);

		verify(mockPurchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	void creditOrDebitFailsForInvalidUser()
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
	void creditSucceeds()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(5);

		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 1);
		assertNotNull(ENABLED_USER_ID + " should be able to purchase 1 token", purchase);

		assertEquals(1, purchase.getQuantity());

		verify(mockPurchaseRepository).save(purchase);
	}

	@Test
	void creditSucceedsWithNoPreviousBalance()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(null);

		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 1);
		assertNotNull(ENABLED_USER_ID + " should be able to purchase 1 token", purchase);

		assertEquals(1, purchase.getQuantity());

		verify(mockPurchaseRepository).save(purchase);
	}

	@Test
	void debitSucceeds()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(5);

		Purchase purchase = tokenManagementService.debitAccount(ENABLED_USER_ID, 1);
		assertNotNull(ENABLED_USER_ID + " should be able to spend 1 token", purchase);

		assertEquals(-1, purchase.getQuantity());

		verify(mockPurchaseRepository).save(purchase);
	}

}