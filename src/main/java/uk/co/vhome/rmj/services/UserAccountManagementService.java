package uk.co.vhome.rmj.services;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.security.Role;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Service interface for user account management
 */
public interface UserAccountManagementService
{
	@Secured({Role.SYSTEM})
	void createBasicDefaultAccounts();

	@Secured({Role.ANONYMOUS, Role.RUN_AS_NEW_USER})
	UserDetailsEntity registerNewUser(String username, String firstName, String lastName, String password);

	@Secured({Role.ADMIN})
	UserDetailsEntity createUser(String username, String firstName, String lastName, String password, String groupName);

	@Secured({Role.MEMBER})
	void changePassword(String username, String oldPassword, String newPassword);

	@Secured({Role.ADMIN})
	void setUserEnabled(Long id, boolean enable);

	@Secured({Role.SYSTEM})
	void updateLastLogin(String username, long timestamp);

	@Secured({Role.ADMIN})
	List<UserDetailsEntity> findAllUserDetails();

	@Secured({Role.ADMIN})
	Set<UserDetailsEntity> findAllUserDetailsIn(Collection<String> usernames);

	@Secured({Role.ORGANISER, Role.RUN_AS_NEW_USER})
	UserDetailsEntity findUserDetails(String username);

	@Secured({Role.ADMIN})
	Set<UserDetailsEntity> findEnabledAdminDetails();
}