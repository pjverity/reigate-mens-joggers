package uk.co.vhome.rmj.config;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uk.co.vhome.rmj.AuthenticatedUser;
import uk.co.vhome.rmj.services.UserRegistrationService;

import javax.inject.Inject;

/**
 * Event handlers for various application/http events
 */
@Component
public class ApplicationEventHandler
{
	private final UserRegistrationService userRegistrationService;

	@Inject
	public ApplicationEventHandler(UserRegistrationService userRegistrationService)
	{
		this.userRegistrationService = userRegistrationService;
	}

	@EventListener
	public void on(ContextRefreshedEvent contextStartedEvent)
	{
		if (contextStartedEvent.getApplicationContext().getParent() == null)
		{
			AuthenticatedUser.runWithSystemUser(userRegistrationService::initialiseFreshDB);
		}
	}
}
