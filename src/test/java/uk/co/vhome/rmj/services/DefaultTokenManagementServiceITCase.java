package uk.co.vhome.rmj.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.TestContext;
import uk.co.vhome.rmj.entities.Purchase;
import uk.co.vhome.rmj.repositories.PurchaseRepository;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static uk.co.vhome.rmj.UserConfigurations.ENABLED_USER;
import static uk.co.vhome.rmj.UserConfigurations.USER_ID;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestContext.class})
@Transactional
public class DefaultTokenManagementServiceITCase
{
	private DefaultTokenManagementService tokenManagementService;

	@Inject
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
	@Sql({"/schema.sql", "/data.sql"})
	public void purchaseSucceedsWithValidTokenCount()
	{
		when(userAccountManagementService.findUserDetails(USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.purchaseTokens(USER_ID, 1);
		assertNotNull(USER_ID + " should be able to purchase 1 token", purchase);

		assertEquals(9L, (long)tokenManagementService.tokenBalance(USER_ID));
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void deductionSucceedsWithValidTokenCount()
	{
		when(userAccountManagementService.findUserDetails(USER_ID)).thenReturn(ENABLED_USER);

		Purchase purchase = tokenManagementService.deductToken(USER_ID);
		assertNotNull(USER_ID + " should be able to use 1 token", purchase);

		assertEquals(7L, (long)tokenManagementService.tokenBalance(USER_ID));
	}

}
