package uk.co.vhome.rmj.site;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.nio.charset.StandardCharsets;

@Configuration
public class ViewControllerTestConfiguration
{
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

	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean()
	{
		LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
		validatorFactoryBean.setValidationMessageSource(messageSource());
		return validatorFactoryBean;
	}

}
