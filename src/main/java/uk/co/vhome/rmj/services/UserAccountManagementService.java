package uk.co.vhome.rmj.services;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import uk.co.vhome.rmj.security.Role;

import java.util.List;

/**
 * Service interface for user account management
 */
public interface UserAccountManagementService extends ServiceAvailabilityReporter
{
	@Secured({Role.SYSTEM})
	void createBasicDefaultAccounts();

	void registerNewUser(String username, String firstName, String lastName, String password);

	@Secured({Role.ADMIN})
	void createUser(String username, String firstName, String lastName, String password, String groupName);

	@Secured({Role.MEMBER})
	void changePassword(String username, String oldPassword, String newPassword);

	@Secured({Role.ADMIN})
	void updateUser(String username, boolean isEnabled, String removeFromGroup, String addToGroup);

	@Secured({Role.ADMIN})
	List<UserAccountDetails> findAllUserDetails();

	@Secured({Role.ORGANISER})
	UserDetails findUserDetails(String username);

	@Secured({Role.SYSTEM})
	void updateLastLogin(String username, long timestamp);
}