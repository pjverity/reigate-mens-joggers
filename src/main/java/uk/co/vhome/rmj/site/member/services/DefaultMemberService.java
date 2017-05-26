package uk.co.vhome.rmj.site.member.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.repositories.UserDetailsRepository;
import uk.co.vhome.rmj.services.EventManagementService;

import java.util.List;
import java.util.Set;

@Service
public class DefaultMemberService implements MemberService
{
	private final UserDetailsRepository userDetailsRepository;

	private final EventManagementService eventManagementService;

	public DefaultMemberService(UserDetailsRepository userDetailsRepository, EventManagementService eventManagementService)
	{
		this.userDetailsRepository = userDetailsRepository;
		this.eventManagementService = eventManagementService;
	}

	@Override
	@Transactional
	public Set<Event> completedEvents()
	{
		UserDetails principal = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		UserDetailsEntity userDetailsEntity = userDetailsRepository.findByUsername(principal.getUsername());

		Set<Event> events = userDetailsEntity.getEvents();
		events.size();

		return events;
	}

	@Override
	public List<Event> findAllIncompleteEvents()
	{
		return eventManagementService.findAllIncompleteEvents();
	}
}
