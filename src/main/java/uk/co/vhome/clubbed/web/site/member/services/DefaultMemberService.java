package uk.co.vhome.clubbed.web.site.member.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.clubbed.entities.Event;
import uk.co.vhome.clubbed.entities.UserDetailsEntity;
import uk.co.vhome.clubbed.entities.UserEntity;
import uk.co.vhome.clubbed.eventmanagement.EventManagementService;
import uk.co.vhome.clubbed.usermanagement.UserAccountManagementService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class DefaultMemberService implements MemberService
{
	private final UserAccountManagementService userAccountManagementService;

	private final EventManagementService eventManagementService;

	public DefaultMemberService(UserAccountManagementService userAccountManagementService, EventManagementService eventManagementService)
	{
		this.userAccountManagementService = userAccountManagementService;
		this.eventManagementService = eventManagementService;
	}

	@Override
	@Transactional
	public Set<Event> completedEvents()
	{
		UserDetails principal = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		UserEntity entity = userAccountManagementService.findUser(principal.getUsername());
		UserDetailsEntity userDetailsEntity = entity.getUserDetailsEntity();
		Set<Event> events = userDetailsEntity.getEvents();
		events.size();

		return events;
	}

	@Override
	public List<Event> findUpcomingEvents()
	{
		return eventManagementService.fetchEventsAfter(LocalDateTime.now(), true, false);
	}
}
