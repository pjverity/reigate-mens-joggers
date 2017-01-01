package uk.co.vhome.rmj.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Map;

@Configuration
@ComponentScan(
		basePackages = "uk.co.vhome",
		excludeFilters = @ComponentScan.Filter(Controller.class)
)
@EnableJpaRepositories(     //p.651
		basePackages = "uk.co.vhome.rmj.repositories",
		entityManagerFactoryRef = "entityManagerFactoryBean",
		transactionManagerRef = "jpaTransactionManager"
)
@EnableTransactionManagement //p.607
public class RootContextConfiguration // implements TransactionManagementConfigurer
{
	private static final String HIBERNATE_DIALECT = "org.hibernate.dialect.PostgreSQL94Dialect";

	private static final String SCHEMA_GENERATION_KEY = "javax.persistence-schema-generation.database.action";

	// Configure our Datasource (ie, the connection to the Database) See p.602
	@Bean
	public DataSource springJpaDataSource()
	{
		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		return lookup.getDataSource("jdbc/RMJ");
	}

	// Configure the persistence unit (Which managers our entities and configures the JPA implementation
	// (Hibernate O/RM being the chosen JPA implementation) (p604)
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean()
	{
		Map<String, Object> properties = new Hashtable<>();
		properties.put(SCHEMA_GENERATION_KEY, "none");

		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabasePlatform(HIBERNATE_DIALECT);

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setJpaVendorAdapter(adapter);
		factoryBean.setDataSource(springJpaDataSource());
		factoryBean.setPackagesToScan("uk.co.vhome.rmj");
		factoryBean.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
		factoryBean.setValidationMode(ValidationMode.NONE);
		factoryBean.setJpaPropertyMap(properties);
		return factoryBean;
	}

	@Bean
	public PlatformTransactionManager jpaTransactionManager()
	{
		return new JpaTransactionManager(entityManagerFactoryBean().getObject());
	}

	/**
	 * Used by the validators to supply internationalised error messages
	 */
	@Bean
	public MessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setCacheSeconds(-1);
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		messageSource.setBasenames(
				"/WEB-INF/il8n/titles", "/WEB-INF/il8n/messages",
				"/WEB-INF/il8n/errors", "/WEB-INF/il8n/validation"
		);
		return messageSource;
	}

	/**
	 * Support for bean validation
	 */
	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean()
	{
		LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
		validatorFactoryBean.setValidationMessageSource(messageSource());
		return validatorFactoryBean;
	}

	/**
	 * Create a post processor which is used to proxy the execution of validation methods (p.449)
	 * This will look for methods annotated with @Validated or @ValidateOnExecution and proxies
	 * them so that validation on annotated parameters and return values are executued at the right time
	 * (before and after method execution)
	 *
	 * Ensure it uses our configured bean validator rather than the default on on the classpath
	 * whcih isn't configured to use a message source
	 */
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor()
	{
		MethodValidationPostProcessor validationPostProcessor = new MethodValidationPostProcessor();
		validationPostProcessor.setValidator(localValidatorFactoryBean().getValidator());
		return validationPostProcessor;
	}
	/**
	 * Used to validate reCaptcha responses
	 */
	@Bean
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}

	// TODO - Find our why this doesn't work, causes a circular reference
	// Protect against Spring choosing the wrong transaction manager if we create several (p.609)
/*
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager()
	{
		LOGGER.traceEntry();

		return txManager();
	}
*/
}
