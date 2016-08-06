package uk.co.vhome.rmj.services;

import uk.co.vhome.rmj.model.User;

public interface UserService
{
	Iterable<User> getAllUsers();

	boolean signUp(String emailAddress, String password);
}