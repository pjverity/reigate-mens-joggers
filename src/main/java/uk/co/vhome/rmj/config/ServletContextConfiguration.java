package uk.co.vhome.rmj.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.servlet.config.annotation.*;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

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

	/*
	 * /images are not packaged with the war, but are served by Tomcat from a directory on the filesystem
     * This is configured in a /Context/Resources/PreResource element in context.xml.
	 *
	 * The element accepts a filesystem path and a url that will cause the container to load
	 * the resource from the filesystem rather than from the contents of the web app.
	 */
	public static final String[] ADDITIONAL_RESOURCE_MATCHERS = {"/css/**",
	                                                             "/galleria/**",
	                                                             "/images/**",
	                                                             "/js/**",
	                                                             "/sitemap.xml"};

	private static final String[] ADDITIONAL_RESOURCE_LOCATIONS = {"/css/",
	                                                               "/galleria/",
	                                                               "/images/",
	                                                               "/js/",
	                                                               "/"};


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

	/*
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

	/*
	 * For the statically served resources, we want to impose some kind of local browser caching
	 * policy so that things that rarely change do not have to be served again. Especially important
	 * with large resources like images.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler(ADDITIONAL_RESOURCE_MATCHERS)
				.addResourceLocations(ADDITIONAL_RESOURCE_LOCATIONS)
				.setCacheControl(CacheControl.maxAge(15, TimeUnit.DAYS).cachePublic());
	}
}
