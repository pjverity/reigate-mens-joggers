package uk.co.vhome.clubbed.usermanagement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import uk.co.vhome.clubbed.InitialSiteUser;
import uk.co.vhome.clubbed.JpaITConfiguration;
import uk.co.vhome.clubbed.entities.UserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class, showSql = false)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=none")
@Import({JpaITConfiguration.class, DefaultUserAccountManagementService.class})
class DefaultUserAccountManagementServiceITCase
{
	@Autowired
	private UserAccountManagementService userAccountManagementService;

	@Test
	@DisplayName("Initial user created")
	@Sql({"/user-schema.sql", "/group-authorities.sql"})
	void createsInitialUsers(@Autowired InitialSiteUser initialSiteUser)
	{
		userAccountManagementService.createBasicDefaultAccounts();

		UserEntity userEntity = userAccountManagementService.findUser(initialSiteUser.getId());

		assertEquals(initialSiteUser.getId(), userEntity.getUsername());
		assertEquals(initialSiteUser.getFirstName(), userEntity.getUserDetailsEntity().getFirstName());
		assertEquals(initialSiteUser.getLastName(), userEntity.getUserDetailsEntity().getLastName());

	}
}
