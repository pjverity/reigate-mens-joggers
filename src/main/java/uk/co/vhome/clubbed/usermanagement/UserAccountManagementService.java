package uk.co.vhome.clubbed.usermanagement;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.clubbed.entities.UserEntity;

import java.util.Collection;
import java.util.List;

/**
 * Service interface for user account management
 */
public interface UserAccountManagementService
{
	@Secured({Role.SYSTEM})
	void createBasicDefaultAccounts();

	@Secured({Role.SYSTEM})
	void updateLastLogin(String username, long timestamp);


	@Secured({Role.ADMIN})
	UserEntity createUser(String username, String firstName, String lastName, String password, String groupName);

	@Secured({Role.ADMIN})
	void setUserEnabled(Long id, boolean enable);

	@Secured({Role.ADMIN})
	List<UserEntity> findUsers(Collection<Long> userIds);

	@Secured({Role.ADMIN})
	Collection<UserEntity> enabledUsersInGroup(boolean enabled, String group);


	@Secured({Role.MEMBER})
	void changePassword(String username, String oldPassword, String newPassword);

	@Secured({Role.MEMBER})
	UserEntity findUser(Long userId);

	@Secured({Role.MEMBER})
	UserEntity findUser(String username);

	@Secured({Role.MEMBER})
	List<UserEntity> findAllUsers();

	UserEntity save(UserEntity userDetails);

}