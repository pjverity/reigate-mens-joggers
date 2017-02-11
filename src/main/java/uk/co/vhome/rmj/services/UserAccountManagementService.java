package uk.co.vhome.rmj.services;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.rmj.Role;

import java.util.List;

/**
 * Service interface for user account management
 */
public interface UserAccountManagementService extends ServiceAvailabilityReporter
{
	@Secured({Role.SYSTEM})
	void createBasicDefaultAccounts();

	void registerNewUser(String userId, String firstName, String lastName, String password);

	@Secured({Role.ADMIN})
	void createUser(String userId, String firstName, String lastName, String password, String groupName);

	@Secured({Role.MEMBER})
	void changePassword(String userId, String oldPassword, String newPassword);

	@Secured({Role.ADMIN})
	void setIsUserEnabled(String userId, boolean isEnabled);

	@Secured({Role.ADMIN})
	List<UserAccountDetails> getUserDetails();

}