package uk.co.vhome.rmj.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.inject.Inject;

@Configuration
@EnableWebMvc
@ComponentScan(
		basePackages = "uk.co.vhome",
		useDefaultFilters = false,
		includeFilters = @ComponentScan.Filter(Controller.class)
)
@EnableSpringDataWebSupport
public class ServletContextConfiguration extends WebMvcConfigurerAdapter
{
	public static final String USER_ID_SESSION_ATTRIBUTE = "userId";

	public static final String USER_FIRST_NAME_SESSION_ATTRIBUTE = "userFirstName";

	public static final String USER_LAST_NAME_SESSION_ATTRIBUTE = "userLastName";

	@Inject
	private SpringValidatorAdapter springValidatorAdapter;

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry)
	{
		registry.jsp().prefix("/WEB-INF/jsp/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry)
	{
		registry.addViewController("/world/coach-profile").setViewName("world/coach-profile");
	}

	/**
	 * Spring MVC Controller form objects and argument validators use a Spring Validator rather than
	 * the javax.?.Validator. Spring MVC creates it's own Validator instance by default, which masks
	 * the one created in the root application context. To ensure we use the one in the root app context,
	 * override this method to return our one.
	 * See p.450
	 */
	@Override
	public Validator getValidator()
	{
		return springValidatorAdapter;
	}

}
