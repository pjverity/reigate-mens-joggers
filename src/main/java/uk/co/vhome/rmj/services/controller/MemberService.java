package uk.co.vhome.rmj.services.controller;

import uk.co.vhome.rmj.entities.Event;

import java.util.List;
import java.util.Set;

public interface MemberService
{
	Set<Event> completedEvents();

	List<Event> findAllIncompleteEvents();
}
