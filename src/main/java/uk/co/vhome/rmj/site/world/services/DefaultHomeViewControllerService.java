package uk.co.vhome.rmj.site.world.services;

import org.springframework.stereotype.Service;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.services.EventManagementService;
import uk.co.vhome.rmj.services.TokenManagementService;
import uk.co.vhome.rmj.services.UserAccountManagementService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DefaultHomeViewControllerService implements HomeViewControllerService
{
	private final UserAccountManagementService userAccountManagementService;

	private final TokenManagementService tokenManagementService;

	private final EventManagementService eventManagementService;

	public DefaultHomeViewControllerService(UserAccountManagementService userAccountManagementService, TokenManagementService tokenManagementService, EventManagementService eventManagementService)
	{
		this.userAccountManagementService = userAccountManagementService;
		this.tokenManagementService = tokenManagementService;
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
		UserDetailsEntity userEntity = userAccountManagementService.registerNewUser(username, firstName, lastName, password);

		tokenManagementService.creditAccount(userEntity.getId(), 1);

		return userEntity;
	}

}
