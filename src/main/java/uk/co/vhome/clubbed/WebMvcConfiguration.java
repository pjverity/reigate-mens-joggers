package uk.co.vhome.clubbed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.*;
import uk.co.vhome.clubbed.security.SecurityConfiguration;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer
{

	private static final String[] ADDITIONAL_RESOURCE_LOCATIONS = {"/css/",
	                                                               "/galleria/",
	                                                               "/images/",
	                                                               "/js/",
	                                                               "/"};

	private final MessageSource messageSource;

	@Autowired
	public WebMvcConfiguration(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry)
	{
		registry.addViewController("/world/coach-profile").setViewName("world/coach-profile");
	}

	/*
	 * For the statically served resources, we want to impose some kind of local browser caching
	 * policy so that things that rarely change do not have to be served again. Especially important
	 * with large resources like images.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler(SecurityConfiguration.UNPROTECTED_RESOURCE_MATCHERS)
				.addResourceLocations(ADDITIONAL_RESOURCE_LOCATIONS)
				.setCacheControl(CacheControl.maxAge(15, TimeUnit.DAYS).cachePublic());
	}

	@Override
	public Validator getValidator()
	{
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
		factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
		factoryBean.setValidationMessageSource(messageSource);
		return factoryBean;
	}
}
