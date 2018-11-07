package uk.co.vhome.clubbed.web.site.member.services;

import uk.co.vhome.clubbed.entities.Event;

import java.util.List;
import java.util.Set;

public interface MemberService
{
	Set<Event> completedEvents();

	List<Event> findUpcomingEvents();
}
