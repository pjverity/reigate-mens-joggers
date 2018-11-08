package uk.co.vhome.clubbed.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.TestTransaction;
import uk.co.vhome.clubbed.JpaITConfiguration;
import uk.co.vhome.clubbed.entities.UserDetailsEntity;
import uk.co.vhome.clubbed.entities.UserEntity;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static uk.co.vhome.clubbed.UserConfigurations.*;

// Prevent DB migrations from taking place
@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class, showSql = false)
// Change the default 'create-drop' strategy from to avoid a Derby specific issue (https://github.com/spring-projects/spring-boot/issues/7706)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=none")
@Import(JpaITConfiguration.class)
class UserRepositoryITCase
{
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private JdbcUserDetailsManager jdbcUserDetailsManager;

	@Test
	@Sql({"/user-schema.sql", "/test-users.sql"})
	void verifySchemaAndTestDataIsValid()
	{
		UserEntity userEntity = usersRepository.findById(ENABLED_USER.getId()).orElseThrow(AssertionError::new);
		UserDetailsEntity userDetailsEntity = userDetailsRepository.findById(ENABLED_USER.getId()).orElseThrow(AssertionError::new);

		assertEquals(userEntity, userDetailsEntity.getUserEntity());
	}

	@Test
	@Sql(value = "/user-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/drop-schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void userDetailsEntityToUserEntityRelationSharesPK()
	{
		// Create a new user, we use Spring's JdbcUserDetailsManager to handle setting/encoding passwords
		User newUser = new User("new.user@home.co.uk", "password", Collections.emptySet());
		jdbcUserDetailsManager.createUser(newUser);

		// Retrieve the user just created
		UserEntity newUserEntity = usersRepository.findByUsername(newUser.getUsername());
		assertNotNull(newUserEntity);

		// Create new details for this user
		UserDetailsEntity newUserDetailsEntity = new UserDetailsEntity(newUserEntity,
		                                                               "FirstName",
		                                                               "LastName");
		// Save the details
		userDetailsRepository.save(newUserDetailsEntity);

		// Flush the changes to the DB before reading them back.
		// Note that usually the test would rollback changes and we'd start with a clean DB again,
		// but committing the changes means we have to perform the clean up manually with the
		// drop-schema.sql script to avoid stepping on the toes of the other tests!
		TestTransaction.flagForCommit();
		TestTransaction.end();

		TestTransaction.start();

		// Ensure we can read the details back
		usersRepository.findById(newUserEntity.getId())
				.ifPresentOrElse(userEntity -> assertEquals(newUserDetailsEntity, userEntity.getUserDetailsEntity()),
				                 () -> fail("Missing user details"));

		TestTransaction.flagForCommit();
		TestTransaction.end();
	}

	@Test
	@Sql(value = "/user-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/drop-schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void userEntityToUserDetailsEntityRelationSharesPK()
	{
		// Create a new user, we use Spring's JdbcUserDetailsManager to handle setting/encoding passwords
		User newUser = new User("new.user@home.co.uk", "password", Collections.emptySet());
		jdbcUserDetailsManager.createUser(newUser);

		// Retrieve the user just created
		UserEntity newUserEntity = usersRepository.findByUsername(newUser.getUsername());
		assertEquals(newUserEntity.getUsername(), newUser.getUsername());

		// Create new details for this user
		UserDetailsEntity newUserDetailsEntity = new UserDetailsEntity(newUserEntity,
		                                                               "FirstName",
		                                                               "LastName");
		// Associate the user details with the user
		newUserEntity.setUserDetailsEntity(newUserDetailsEntity);

		// Save the user
		userDetailsRepository.save(newUserDetailsEntity);

		// Flush the changes to the DB before reading them back.
		// Note that usually the test would rollback changes and we'd start with a clean DB again,
		// but committing the changes means we have to perform the clean up manually with the
		// drop-schema.sql script to avoid stepping on the toes of the other tests!
		TestTransaction.flagForCommit();
		TestTransaction.end();

		TestTransaction.start();

		// Ensure we can read the details back
		userDetailsRepository.findById(newUserEntity.getId())
				.ifPresentOrElse(userDetailsEntity -> assertEquals(newUserEntity, userDetailsEntity.getUserEntity()),
				                 () -> fail("Missing user details"));

		TestTransaction.flagForCommit();
		TestTransaction.end();
	}

	@Test
	@Sql({"/user-schema.sql", "/test-users.sql"})
	void loadsExpectedEnabledUsersInGroup()
	{
		Collection<Integer> enabledAdmins = usersRepository.enabledUsersInGroup(true, "ADMIN");

		assertIterableEquals(Set.of(ADMIN_USER.getId().intValue()), enabledAdmins);

		Collection<Integer> disabledMembers = usersRepository.enabledUsersInGroup(false, "MEMBER");

		assertIterableEquals(Set.of(DISABLED_USER.getId().intValue()), disabledMembers);
	}

	@Test
	@Sql({"/user-schema.sql", "/test-users.sql"})
	void updatesLastLoginTime()
	{
		Instant now = Instant.now();

		usersRepository.updateLastLogin(ENABLED_USER.getUsername(), now);

		usersRepository.findById(ENABLED_USER.getId())
				.ifPresentOrElse(userEntity -> Assertions.assertEquals(userEntity.getLastLogin(), now),
				                 () -> fail("Expected user not found"));
	}
}