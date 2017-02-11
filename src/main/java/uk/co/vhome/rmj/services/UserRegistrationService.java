package uk.co.vhome.rmj.services;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.rmj.Role;

/**
 * Interface for services pertaining to registering new users
 */
public interface UserRegistrationService extends ServiceAvailabilityReporter
{
	@Secured({Role.SYSTEM})
	void initialiseFreshDB();

	@Secured({Role.SYSTEM, Role.ADMIN})
	void createUser(String userId, String firstName, String lastName, String password, String groupName);

	void registerNewUser(String userId, String firstName, String lastName, String password);

	@Secured({Role.ADMIN, Role.ORGANISER, Role.MEMBER})
	void changePassword(String userId, String oldPassword, String newPassword);
}