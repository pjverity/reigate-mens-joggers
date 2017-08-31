package uk.co.vhome.rmj.site.member.services;

import uk.co.vhome.clubbed.domainobjects.entities.Event;

import java.util.List;
import java.util.Set;

public interface MemberService
{
	Set<Event> completedEvents();

	List<Event> findUpcomingEvents();
}
