package uk.co.vhome.rmj.services;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
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
	void setUserEnabled(Long id, boolean enable);

	@Secured({Role.SYSTEM})
	void updateLastLogin(String username, long timestamp);

	@Secured({Role.ADMIN})
	List<UserDetailsEntity> findAllUserDetails();

	@Secured({Role.MEMBER})
	UserDetailsEntity findUserDetails(String username);
}