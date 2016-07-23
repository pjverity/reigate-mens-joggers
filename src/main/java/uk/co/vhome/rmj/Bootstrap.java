package uk.co.vhome.rmj;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import uk.co.vhome.rmj.config.RootContextConfiguration;
import uk.co.vhome.rmj.config.ServletContextConfiguration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * This is called once when the container initialises the application, before any Listeners
 * are called. This is the earliest point of initialisation. How does it get called? Well...
 *
 * Servlet 3.0+ containers use the Java Service Provider API to locate classes that
 * implement the ServletContainerInitializer interface. It then instantiates them
 * and calls their onStartup() method. To hook in to Java's SPI loading framework so
 * that the container can call our bootstrapper requires packaging a JAR
 * containing a META-INF/services/javax.servlet.ServletContainerInitializer file that
 * lists the FQN of the concrete classes in the JAR implementing that interface. This
 * JAR then has to be packaged and placed in the web applications /lib directory.
 *
 * To avoid all the pain of creating and deploying that JAR, the spring-web JAR on the
 * applications class path already provides this via the SpringServletContainerInitializer.
 * The JAR contains the META-INF/services file listing SpringServletContainerInitializer as the
 * concrete class implementing ServletContainerInitializer.
 *
 * The implementation of SpringServletContainerInitializer declares that it can handle classes
 * of type WebApplicationInitializer. The container scans for classes implementing this
 * and provides them to the onStartup() method, SpringServletContainerInitializer then
 * instantiates them and calls onStartup() on those instances... So, all we have to do
 * is implement WebApplicationInitializer and allow classpath scanning to find us and
 * for SpringServletContainerInitializer to call us!
 *
 * And so that is how we get to initialise our Spring app contexts, Servlets, Filters etc
 * programmatically at start-up!
 */
@SuppressWarnings("unused")
public class Bootstrap implements WebApplicationInitializer
{

	private static final String DEFAULT_SERVLET_NAME = "default";

	private static final String SERVLET_DISPATCHER_NAME = "springDispatcher";

	public void onStartup(ServletContext servletContext) throws ServletException
	{
		servletContext.getServletRegistration(DEFAULT_SERVLET_NAME).addMapping("/css/*", "/font-awesome-4.6.3/*", "/sitemap.xml");

		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(RootContextConfiguration.class);
		servletContext.addListener(new ContextLoaderListener(rootContext));

		AnnotationConfigWebApplicationContext servletContextConfig = new AnnotationConfigWebApplicationContext();
		servletContextConfig.register(ServletContextConfiguration.class);
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet(SERVLET_DISPATCHER_NAME, new DispatcherServlet(servletContextConfig));

		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}
}
