package uk.co.vhome.rmj.site.world.services;

import org.springframework.stereotype.Service;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.services.core.EventManagementService;
import uk.co.vhome.rmj.services.core.UserAccountManagementService;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DefaultHomeViewControllerService implements HomeViewControllerService
{
	private final UserAccountManagementService userAccountManagementService;

	private final EventManagementService eventManagementService;

	@Inject
	public DefaultHomeViewControllerService(UserAccountManagementService userAccountManagementService, EventManagementService eventManagementService)
	{
		this.userAccountManagementService = userAccountManagementService;
		this.eventManagementService = eventManagementService;
	}

	@Override
	public Optional<Event> findNextEvent()
	{
		return eventManagementService.fetchEventsAfter(LocalDateTime.now(), true, false).stream().findFirst();
	}

	@Override
	public UserDetailsEntity registerNewUser(String username, String firstName, String lastName, String password)
	{
		return userAccountManagementService.registerNewUser(username, firstName, lastName, password);
	}

}
