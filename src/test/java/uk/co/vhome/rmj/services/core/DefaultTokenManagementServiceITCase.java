package uk.co.vhome.rmj.services.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.clubbed.domainobjects.entities.MemberBalance;
import uk.co.vhome.clubbed.domainobjects.entities.Purchase;
import uk.co.vhome.clubbed.notifications.BalanceUpdatedNotification;
import uk.co.vhome.clubbed.notifications.LowBalanceNotification;
import uk.co.vhome.clubbed.notifications.services.NotificationService;
import uk.co.vhome.clubbed.paymentmanagement.DefaultTokenManagementService;
import uk.co.vhome.clubbed.paymentmanagement.TokenManagementService;
import uk.co.vhome.clubbed.web.services.usermanagement.UserAccountManagementService;
import uk.co.vhome.rmj.IntegrationTestConfiguration;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static uk.co.vhome.rmj.UserConfigurations.ENABLED_USER;
import static uk.co.vhome.rmj.UserConfigurations.ENABLED_USER_ID;

@ActiveProfiles({"integration-test"})
@SpringJUnitConfig(classes = {IntegrationTestConfiguration.class, DefaultTokenManagementService.class})
@Transactional
class DefaultTokenManagementServiceITCase
{
	@Inject
	private UserAccountManagementService mockUserAccountManagementService;

	@Inject
	private TokenManagementService tokenManagementService;

	@Inject
	private NotificationService notificationService;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@AfterEach
	void teardown()
	{
		reset(notificationService);
	}

	@Test
	@Sql({"/schema.sql", "/data.sql", "/purchases.sql"})
	void returnsCorrectBalanceForCredit()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 1);
		assertNotNull(purchase, ENABLED_USER_ID + " should be able to purchase 1 token");

		assertEquals(8L, (long)tokenManagementService.balanceForMember(ENABLED_USER_ID));

		verify(notificationService).postNotification(any(BalanceUpdatedNotification.class));
	}

	@Test
	@Sql({"/schema.sql", "/data.sql", "/purchases.sql"})
	void returnsCorrectBalanceForDebit()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.debitAccount(ENABLED_USER_ID, 1);
		assertNotNull(purchase, ENABLED_USER_ID + " should be able to use 1 token");

		assertEquals(6L, (long)tokenManagementService.balanceForMember(ENABLED_USER_ID));

		verify(notificationService).postNotification(any(BalanceUpdatedNotification.class));
	}

	@Test
	@Sql({"/schema.sql", "/data.sql", "/purchases.sql"})
	void sendsLowBalanceNotification()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.debitAccount(ENABLED_USER_ID, 4);
		assertNotNull(purchase, ENABLED_USER_ID + " should be able to use 1 token");

		assertEquals(3L, (long)tokenManagementService.balanceForMember(ENABLED_USER_ID));

		verify(notificationService).postNotification(any(LowBalanceNotification.class));
	}

	@Test
	@Sql({"/schema.sql", "/data.sql", "/purchases.sql"})
	void returnsExpectedBalanceForAllEnabledMembers()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		List<MemberBalance> memberBalance = tokenManagementService.balancesForAllEnabledMembers();

		assertEquals(1, memberBalance.size());

		assertEquals(ENABLED_USER_ID, memberBalance.get(0).getUserId());
		assertEquals("Test", memberBalance.get(0).getFirstName());
		assertEquals("User (Enabled)", memberBalance.get(0).getLastName());
		assertEquals(7, (int) memberBalance.get(0).getBalance());
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	void returnsZeroBalanceForAllEnabledMembersWithNoPurchaseHistory()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		List<MemberBalance> memberBalance = tokenManagementService.balancesForAllEnabledMembers();

		assertEquals(1, memberBalance.size());

		assertEquals(ENABLED_USER_ID, memberBalance.get(0).getUserId());
		assertEquals("Test", memberBalance.get(0).getFirstName());
		assertEquals("User (Enabled)", memberBalance.get(0).getLastName());
		assertNull(memberBalance.get(0).getBalance());
	}

	@Test
	void throwsCreditConstraintViolationWhenCreditQuantityIsZero()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		assertThrows(ConstraintViolationException.class, () -> tokenManagementService.creditAccount(ENABLED_USER_ID, 0));

	}

	@Test
	void throwsCreditConstraintViolationWhenDebitQuantityIsZero()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		assertThrows(ConstraintViolationException.class, () -> tokenManagementService.debitAccount(ENABLED_USER_ID, 0));
	}

}
