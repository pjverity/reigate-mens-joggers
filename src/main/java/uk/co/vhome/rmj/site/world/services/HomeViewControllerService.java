package uk.co.vhome.rmj.site.world.services;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.clubbed.domainobjects.entities.Event;
import uk.co.vhome.clubbed.domainobjects.entities.UserDetailsEntity;
import uk.co.vhome.clubbed.security.Role;
import uk.co.vhome.clubbed.security.RunAs;

import java.util.Optional;

public interface HomeViewControllerService
{
	Optional<Event> findNextEvent();

	@Secured({Role.ANONYMOUS, RunAs.NEW_USER})
	UserDetailsEntity registerNewUser(String username, String firstName, String lastName, String password);

	String flickerGroupNsid();
}
