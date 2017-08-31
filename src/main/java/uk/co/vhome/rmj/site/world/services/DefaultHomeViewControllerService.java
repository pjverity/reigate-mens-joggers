package uk.co.vhome.rmj.site.world.services;

import org.springframework.stereotype.Service;
import uk.co.vhome.clubbed.domainobjects.entities.Event;
import uk.co.vhome.clubbed.domainobjects.entities.UserDetailsEntity;
import uk.co.vhome.clubbed.web.services.UserAccountManagementService;
import uk.co.vhome.rmj.services.core.EventManagementService;
import uk.co.vhome.rmj.services.flickr.FlickrService;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DefaultHomeViewControllerService implements HomeViewControllerService
{
	private final UserAccountManagementService userAccountManagementService;

	private final EventManagementService eventManagementService;

	private final FlickrService flickrService;

	@Inject
	public DefaultHomeViewControllerService(UserAccountManagementService userAccountManagementService, EventManagementService eventManagementService, FlickrService flickrService)
	{
		this.userAccountManagementService = userAccountManagementService;
		this.eventManagementService = eventManagementService;
		this.flickrService = flickrService;
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

	@Override
	public String flickerGroupNsid()
	{
		return flickrService.getCurrentGroupNsid();
	}
}
