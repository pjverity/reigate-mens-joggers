package uk.co.vhome.rmj.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import uk.co.vhome.rmj.model.User;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

public interface UserService extends UserDetailsService
{
	@RolesAllowed("ADMIN")
	Iterable<User> getAllUsers();

	@PermitAll
	boolean memberSignUp(String emailAddress, String password, String firstName, String lastName);
}