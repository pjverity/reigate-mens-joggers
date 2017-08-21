package uk.co.vhome.rmj.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import javax.mail.Session;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableTransactionManagement //p.607
@ComponentScan(
		basePackages = "uk.co.vhome",
		excludeFilters = @ComponentScan.Filter(Controller.class)
)
@EnableJpaRepositories(     //p.651
		basePackages = "uk.co.vhome.rmj.repositories"
)
public class RootContextConfiguration
{
	private static final String POSTGRESQL94_DIALECT = "org.hibernate.dialect.PostgreSQL94Dialect";

	private static final String SCHEMA_GENERATION_KEY = "javax.persistence-schema-generation.database.action";

	private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

	@Bean(initMethod = "migrate")
	public Flyway flyway()
	{
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource());
		return flyway;
	}

	// Configure our Datasource (ie, the connection to the Database) See p.602
	@Bean
	public DataSource dataSource()
	{
		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		return lookup.getDataSource("jdbc/RMJ");
	}

	@Bean
	public String recaptchaSecretKey()
	{
		SimpleJndiBeanFactory simpleJndiBeanFactory = new SimpleJndiBeanFactory();
		return (String) simpleJndiBeanFactory.getBean("recaptcha/SecretKey");
	}

	@Bean
	public InitialSiteUser initialSiteUser()
	{
		SimpleJndiBeanFactory simpleJndiBeanFactory = new SimpleJndiBeanFactory();
		return (InitialSiteUser) simpleJndiBeanFactory.getBean("InitialSiteUser");
	}

/*
	@Bean
	public JavaMailSender javaMailSender()
	{
		SimpleJndiBeanFactory locator = new SimpleJndiBeanFactory();
		Session session = ((Session) locator.getBean("mail/Session"));

		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setSession(session);
		javaMailSender.setDefaultEncoding("UTF-8");

		return javaMailSender;
	}
*/

	// Configure the persistence unit (Which manages our entities and configures the JPA implementation
	// (Hibernate O/RM being the chosen JPA implementation) (p604)
	@Bean
	@DependsOn("flyway")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
	{
		Map<String, Object> properties = new Hashtable<>();
		properties.put(SCHEMA_GENERATION_KEY, "none");
		properties.put("hibernate.default_schema", "public");
		properties.put("hibernate.jdbc.time_zone", "UTC");

		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabasePlatform(POSTGRESQL94_DIALECT);

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setJpaVendorAdapter(adapter);
		factoryBean.setDataSource(dataSource());
		factoryBean.setPackagesToScan("uk.co.vhome.rmj.entities");
		factoryBean.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
		factoryBean.setValidationMode(ValidationMode.NONE);
		factoryBean.setJpaPropertyMap(properties);
		return factoryBean;
	}

	@Bean
	//@Primary - Uncomment if more than one PlatformTransactionManager is present, for example if
	// a bean creates a DataSourceTransactionManager. This annotation is preferred
	// vs implementing TransactionManagementConfigurer.
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory)
	{
		return new JpaTransactionManager(entityManagerFactory);
	}

	/*
	 * Used in places where granular transaction management is required
	 */
	@Bean
	TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager)
	{
		return new TransactionTemplate(platformTransactionManager);
	}

	/*
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

	/*
	 * Support for bean validation
	 */
	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean()
	{
		LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
		validatorFactoryBean.setValidationMessageSource(messageSource());
		return validatorFactoryBean;
	}

	/*
	 * Create a post processor which is used to proxy the execution of validation methods (p.449)
	 * This will look for methods annotated with @Validated or @ValidateOnExecution and proxies
	 * them so that validation on annotated parameters and return values are executed at the right time
	 * (before and after method execution)
	 * <p>
	 * Ensure it uses our configured bean validator rather than the default on on the classpath
	 * which isn't configured to use a message source
	 * </p>
	 */
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor()
	{
		MethodValidationPostProcessor validationPostProcessor = new MethodValidationPostProcessor();
		validationPostProcessor.setValidator(localValidatorFactoryBean().getValidator());
		return validationPostProcessor;
	}

	/*
	 * Used to validate reCaptcha responses
	 */
	@Bean
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}

	@Bean
	public RestTemplate flickrRestTemplate()
	{
		SimpleJndiBeanFactory simpleJndiBeanFactory = new SimpleJndiBeanFactory();
		String apiKey = (String) simpleJndiBeanFactory.getBean("flickr/ApiKey");

		RestTemplate restTemplate = new RestTemplate();

		Map<String, String> defaultVariables = new HashMap<>();
		defaultVariables.put("apiKey", apiKey);

		restTemplate.setDefaultUriVariables(defaultVariables);

		return restTemplate;
	}

	@Bean
	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration()
	{
		FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
		bean.setTemplateLoaderPath("/WEB-INF/fmtemplates");
		return bean;
	}

	@Bean
	ExecutorService executorService()
	{
		return EXECUTOR_SERVICE;
	}
}
