package uk.co.vhome.rmj.services.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.IntegrationTestConfiguration;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.notifications.BalanceUpdatedNotification;
import uk.co.vhome.rmj.notifications.LowBalanceNotification;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static uk.co.vhome.rmj.UserConfigurations.ENABLED_USER;
import static uk.co.vhome.rmj.UserConfigurations.ENABLED_USER_ID;

@RunWith(SpringRunner.class)
@ActiveProfiles({"integration-test"})
@ContextConfiguration(classes = {IntegrationTestConfiguration.class, DefaultTokenManagementService.class})
@Transactional
public class DefaultTokenManagementServiceITCase
{
	@Inject
	private UserAccountManagementService mockUserAccountManagementService;

	@Inject
	private TokenManagementService tokenManagementService;

	@Inject
	private NotificationService notificationService;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void teardown()
	{
		reset(notificationService);
	}

	@Test
	@Sql({"/schema.sql", "/data.sql", "/purchases.sql"})
	public void returnsCorrectBalanceForCredit()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.creditAccount(ENABLED_USER_ID, 1);
		assertNotNull(ENABLED_USER_ID + " should be able to purchase 1 token", purchase);

		assertEquals(8L, (long)tokenManagementService.balanceForMember(ENABLED_USER_ID));

		verify(notificationService).postNotification(any(BalanceUpdatedNotification.class));
	}

	@Test
	@Sql({"/schema.sql", "/data.sql", "/purchases.sql"})
	public void returnsCorrectBalanceForDebit()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.debitAccount(ENABLED_USER_ID, 1);
		assertNotNull(ENABLED_USER_ID + " should be able to use 1 token", purchase);

		assertEquals(6L, (long)tokenManagementService.balanceForMember(ENABLED_USER_ID));

		verify(notificationService).postNotification(any(BalanceUpdatedNotification.class));
	}

	@Test
	@Sql({"/schema.sql", "/data.sql", "/purchases.sql"})
	public void sendsLowBalanceNotification()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.debitAccount(ENABLED_USER_ID, 4);
		assertNotNull(ENABLED_USER_ID + " should be able to use 1 token", purchase);

		assertEquals(3L, (long)tokenManagementService.balanceForMember(ENABLED_USER_ID));

		verify(notificationService).postNotification(any(LowBalanceNotification.class));
	}

	@Test
	@Sql({"/schema.sql", "/data.sql", "/purchases.sql"})
	public void returnsExpectedBalanceForAllEnabledMembers()
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
	public void returnsZeroBalanceForAllEnabledMembersWithNoPurchaseHistory()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		List<MemberBalance> memberBalance = tokenManagementService.balancesForAllEnabledMembers();

		assertEquals(1, memberBalance.size());

		assertEquals(ENABLED_USER_ID, memberBalance.get(0).getUserId());
		assertEquals("Test", memberBalance.get(0).getFirstName());
		assertEquals("User (Enabled)", memberBalance.get(0).getLastName());
		assertNull(memberBalance.get(0).getBalance());
	}

	@Test(expected = ConstraintViolationException.class)
	public void throwsCreditConstraintViolationWhenCreditQuantityIsZero()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		tokenManagementService.creditAccount(ENABLED_USER_ID, 0);
	}

	@Test(expected = ConstraintViolationException.class)
	public void throwsCreditConstraintViolationWhenDebitQuantityIsZero()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		tokenManagementService.debitAccount(ENABLED_USER_ID, 0);
	}

}
