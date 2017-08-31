package uk.co.vhome.rmj.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;
import uk.co.vhome.clubbed.db.InitialSiteUser;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan(
		basePackages = "uk.co.vhome",
		excludeFilters = @ComponentScan.Filter(Controller.class)
)
public class RootContextConfiguration
{

	private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

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
