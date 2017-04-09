package uk.co.vhome.rmj.services.controller;

import uk.co.vhome.rmj.entities.Event;

import java.util.Optional;

public interface HomeViewControllerService
{
	Optional<Event> findNextEvent();

	void registerNewUser(String username, String firstName, String lastName, String password);
}
