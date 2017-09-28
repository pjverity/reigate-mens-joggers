package uk.co.vhome.rmj.services.core;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.vhome.clubbed.dataaccess.repositories.PurchaseRepository;
import uk.co.vhome.clubbed.dataaccess.repositories.UserDetailsRepository;
import uk.co.vhome.clubbed.domainobjects.entities.Purchase;
import uk.co.vhome.clubbed.notifications.services.NotificationService;
import uk.co.vhome.clubbed.paymentmanagement.DefaultTokenManagementService;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static uk.co.vhome.rmj.UserConfigurations.*;

class DefaultTokenManagementServiceTest
{
	@Mock
	private EntityManager mockEntityManager;

	@Mock
	private PurchaseRepository mockPurchaseRepository;

	@Mock
	private UserDetailsRepository mockUserDetailsRepository;

	@Mock
	private NotificationService notificationService;

	private DefaultTokenManagementService tokenManagementService;

	@BeforeEach
	void init()
	{
		MockitoAnnotations.initMocks(this);

		tokenManagementService = new DefaultTokenManagementService(mockPurchaseRepository, mockUserDetailsRepository, mockEntityManager, notificationService);
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
	void creditOrDebitFailsForInvalidUser()
	{
		Long randomUserId = 4L;

		when(mockUserDetailsRepository.findById(ENABLED_USER_ID)).thenReturn(Optional.of(ENABLED_USER));

		Purchase purchase;

		// Unknown user - Credit
		Throwable exception = assertThrows(RuntimeException.class, () ->
		{
			when(mockUserDetailsRepository.findById(randomUserId)).thenReturn(Optional.empty());
			Purchase _purchase = tokenManagementService.creditAccount(randomUserId, 5);
			assertNull(_purchase, randomUserId + " should not be able to purchase tokens");
		});

		assertEquals("Unable to find userId: " + randomUserId, exception.getMessage());

		// Unknown user - Debit
		exception = assertThrows(RuntimeException.class, () ->
		{
			when(mockUserDetailsRepository.findById(randomUserId)).thenReturn(Optional.empty());
			Purchase _purchase = tokenManagementService.debitAccount(randomUserId, 1);
			assertNull(_purchase, randomUserId + " should not be able to purchase tokens");
		});

		assertEquals("Unable to find userId: " + randomUserId, exception.getMessage());

		// Disabled user - Credit
		when(mockUserDetailsRepository.findById(DISABLED_USER_ID)).thenReturn(Optional.of(DISABLED_USER));
		purchase = tokenManagementService.creditAccount(DISABLED_USER_ID, 5);
		assertNull(purchase, DISABLED_USER_ID + " should not be able to purchase tokens");

		// Disabled user - Debit
		when(mockUserDetailsRepository.findById(DISABLED_USER_ID)).thenReturn(Optional.of(DISABLED_USER));
		purchase = tokenManagementService.debitAccount(DISABLED_USER_ID, 1);
		assertNull(purchase, DISABLED_USER_ID + " should not be able to purchase tokens");

		verify(mockPurchaseRepository, never()).save(any(Purchase.class));
	}

	@Test
	void creditSucceeds()
	{
		when(mockUserDetailsRepository.findById(ENABLED_USER_ID)).thenReturn(Optional.of(ENABLED_USER));
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(5);

		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 1);
		assertNotNull(purchase, ENABLED_USER_ID + " should be able to purchase 1 token");

		assertEquals(1, purchase.getQuantity());

		verify(mockPurchaseRepository).save(purchase);
	}

	@Test
	void creditSucceedsWithNoPreviousBalance()
	{
		when(mockUserDetailsRepository.findById(ENABLED_USER_ID)).thenReturn(Optional.of(ENABLED_USER));
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(null);

		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 1);
		assertNotNull(purchase, ENABLED_USER_ID + " should be able to purchase 1 token");

		assertEquals(1, purchase.getQuantity());

		verify(mockPurchaseRepository).save(purchase);
	}

	@Test
	void debitSucceeds()
	{
		when(mockUserDetailsRepository.findById(ENABLED_USER_ID)).thenReturn(Optional.of(ENABLED_USER));
		when(mockPurchaseRepository.calculateBalanceForUser(ENABLED_USER_ID)).thenReturn(5);

		Purchase purchase = tokenManagementService.debitAccount(ENABLED_USER_ID, 1);
		assertNotNull(purchase, ENABLED_USER_ID + " should be able to spend 1 token");

		assertEquals(-1, purchase.getQuantity());

		verify(mockPurchaseRepository).save(purchase);
	}

}