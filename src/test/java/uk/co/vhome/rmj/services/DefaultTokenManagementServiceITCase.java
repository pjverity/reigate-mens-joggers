package uk.co.vhome.rmj.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.IntegrationTestConfiguration;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.repositories.PurchaseRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static uk.co.vhome.rmj.UserConfigurations.ENABLED_USER;
import static uk.co.vhome.rmj.UserConfigurations.ENABLED_USER_ID;

@RunWith(SpringRunner.class)
@ActiveProfiles({"integration-test"})
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
@Transactional
public class DefaultTokenManagementServiceITCase
{
	@Mock
	private UserAccountManagementService mockUserAccountManagementService;

	@Inject
	private PurchaseRepository purchaseRepository;

	@Inject
	private EntityManager entityManager;

	private DefaultTokenManagementService tokenManagementService;

	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);

		tokenManagementService = new DefaultTokenManagementService(purchaseRepository, mockUserAccountManagementService, entityManager);
		tokenManagementService.setBalanceLowerLimit(-5);
		tokenManagementService.setBalanceUpperLimit(20);
		tokenManagementService.setPurchaseLimit(10);
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void positiveBalanceAdjustmentSucceeds()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.modifyBalance(ENABLED_USER_ID, 1);
		assertNotNull(ENABLED_USER_ID + " should be able to purchase 1 token", purchase);

		assertEquals(8L, (long)tokenManagementService.balanceForMember(ENABLED_USER_ID));
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void negativeBalanceAdjustmentSucceeds()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.modifyBalance(ENABLED_USER_ID, -1);
		assertNotNull(ENABLED_USER_ID + " should be able to use 1 token", purchase);

		assertEquals(6L, (long)tokenManagementService.balanceForMember(ENABLED_USER_ID));
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void balanceForAllEnabledMembers()
	{
		when(mockUserAccountManagementService.findUserDetails(ENABLED_USER_ID)).thenReturn(ENABLED_USER);

		List<MemberBalance> memberBalance = tokenManagementService.balanceForAllEnabledMembers();

		assertEquals(1, memberBalance.size());

		assertEquals(ENABLED_USER_ID, memberBalance.get(0).getUsername());
		assertEquals("Test", memberBalance.get(0).getFirstName());
		assertEquals("User (Enabled)", memberBalance.get(0).getLastName());
		assertEquals(7L, (long) memberBalance.get(0).getQuantity());
	}

}
