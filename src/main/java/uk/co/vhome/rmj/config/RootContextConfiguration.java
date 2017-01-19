package uk.co.vhome.rmj.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement(order = Ordered.LOWEST_PRECEDENCE) //p.607
@ComponentScan(
		basePackages = "uk.co.vhome",
		excludeFilters = @ComponentScan.Filter(Controller.class)
)
@EnableJpaRepositories(     //p.651
		basePackages = "uk.co.vhome.rmj.repositories",
		entityManagerFactoryRef = "entityManagerFactoryBean",
		transactionManagerRef = "jpaTransactionManager"
)
@PropertySource("file:rmj-${env}.properties")
public class RootContextConfiguration //implements TransactionManagementConfigurer
{
	private static final String HIBERNATE_DIALECT = "org.hibernate.dialect.PostgreSQL94Dialect";

	private static final String SCHEMA_GENERATION_KEY = "javax.persistence-schema-generation.database.action";

	// Configure our Datasource (ie, the connection to the Database) See p.602
	@Bean
	public DataSource dataSource()
	{
		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		return lookup.getDataSource("jdbc/RMJ");
	}

	// Configure the persistence unit (Which manages our entities and configures the JPA implementation
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
		factoryBean.setDataSource(dataSource());
		factoryBean.setPackagesToScan("uk.co.vhome.rmj");
		factoryBean.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
		factoryBean.setValidationMode(ValidationMode.NONE);
		factoryBean.setJpaPropertyMap(properties);
		return factoryBean;
	}

	@Bean
	@Primary
	public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory)
	{
		return new JpaTransactionManager(entityManagerFactory);
	}

/*
	@Bean
	public PlatformTransactionManager dataSourceTransactionManager()
	{
		return new DataSourceTransactionManager(dataSource());
	}
*/

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
	 * them so that validation on annotated parameters and return values are executed at the right time
	 * (before and after method execution)
	 *
	 * Ensure it uses our configured bean validator rather than the default on on the classpath
	 * which isn't configured to use a message source
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

	@Bean
	public JavaMailSender javaMailSender(@Value("${service.mail.host}") String mailHost,
	                                     @Value("${service.mail.user}") String mailUser,
	                                     @Value("${service.mail.password}") String mailPassword)
	{
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(mailHost);
		mailSender.setPort(25);
		mailSender.setUsername(mailUser);
		mailSender.setPassword(mailPassword);
		mailSender.setDefaultEncoding("UTF-8");

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.auth.mechanisms", "DIGEST-MD5");
		properties.put("mail.smtp.localhost", mailHost);

		mailSender.setJavaMailProperties(properties);

		return mailSender;
	}

	@Bean
	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
		FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
		bean.setTemplateLoaderPath("/WEB-INF/fmtemplates");
		return bean;
	}

	// TODO - Find our why this doesn't work, causes a circular reference
	// Protect against Spring choosing the wrong transaction manager if we create several (p.609)
/*
	@Bean
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager()
	{
		LOGGER.traceEntry();
		return this.jpaTransactionManager();
		return new JpaTransactionManager(entityManagerFactoryBean().getObject());
		return this.dataSourceTransactionManager();
		return jpaTransactionManager;
	}
*/
}
