package uk.co.vhome.rmj;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import uk.co.vhome.rmj.services.core.NotificationService;
import uk.co.vhome.rmj.services.core.UserAccountManagementService;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Basic integration test context
 */
@Profile("integration-test")
@Configuration
@EnableJpaRepositories(
		entityManagerFactoryRef = "testEntityManagerFactory",
		transactionManagerRef = "testTransactionManager")
@EnableTransactionManagement
public class IntegrationTestConfiguration
{
	@Bean
	public DataSource dataSource()
	{
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.setType(EmbeddedDatabaseType.DERBY).setName("test-db").build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean testEntityManagerFactory()
	{
		Properties properties = new Properties();
		properties.put("hibernate.default_schema", "APP");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(false);
		vendorAdapter.setDatabasePlatform("org.hibernate.dialect.DerbyTenSevenDialect");

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setJpaProperties(properties);
		factory.setPackagesToScan("uk.co.vhome.rmj.entities");
		factory.setDataSource(dataSource());

		return factory;
	}

	@Bean
	public PlatformTransactionManager testTransactionManager(EntityManagerFactory entityManagerFactory)
	{
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}

	// Required to test constraint violations. Ensures the service interfaces are proxied for the
	// constraint validation to execute
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	@Bean
	public UserAccountManagementService userAccountManagementService()
	{
		return Mockito.mock(UserAccountManagementService.class);
	}

	@Bean
	public NotificationService notificationService()
	{
		return Mockito.mock(NotificationService.class);
	}
}
