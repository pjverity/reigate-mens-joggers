package uk.co.vhome.rmj.config;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uk.co.vhome.rmj.AuthenticatedUser;
import uk.co.vhome.rmj.services.UserAccountManagementService;

import javax.inject.Inject;

/**
 * Event handlers for various application/http events
 */
@Component
public class ApplicationEventHandler
{
	private final UserAccountManagementService userAccountManagementService;

	@Inject
	public ApplicationEventHandler(UserAccountManagementService userAccountManagementService)
	{
		this.userAccountManagementService = userAccountManagementService;
	}

	@EventListener
	public void on(ContextRefreshedEvent contextStartedEvent)
	{
		if (contextStartedEvent.getApplicationContext().getParent() == null)
		{
			AuthenticatedUser.runWithSystemUser(userAccountManagementService::createBasicDefaultAccounts);
		}
	}
}
