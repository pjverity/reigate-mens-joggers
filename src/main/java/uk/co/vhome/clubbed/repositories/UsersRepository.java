package uk.co.vhome.clubbed.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.clubbed.entities.UserEntity;

import java.time.Instant;
import java.util.Collection;


/**
 * Spring Data repository for accessing and modifying <strong>existing</strong> {@link UserEntity}'s
 * entities. To create new ones or modify the password, query group, authority information etc.,
 * use Springs JdbcUserDetailsManager.
 */
public interface UsersRepository extends JpaRepository<UserEntity, Long>
{
	UserEntity findByUsername(String username);

	@Query(value = "SELECT u.id FROM users u, group_members gm, groups g " +
			               "WHERE u.enabled = ?1 " +
			               "AND u.username = gm.username " +
			               "AND gm.group_id = g.id " +
			               "AND g.group_name = ?2",
			nativeQuery = true)
	Collection<Integer> enabledUsersInGroup(boolean enabled, String groupName);

	@Modifying
	@Query("update UserEntity u set u.lastLogin = :loginTime where u.username = :username")
	@Transactional
	void updateLastLogin(@Param("username") String username, @Param("loginTime") Instant loginTime);
}
