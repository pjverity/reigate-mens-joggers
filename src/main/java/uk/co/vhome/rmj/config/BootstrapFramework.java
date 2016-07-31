package uk.co.vhome.rmj.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * This is called once when the container initialises the application, before any Listeners
 * are called. This is the earliest point of initialisation. How does it get called? Well...
 * <p/>
 * Servlet 3.0+ containers use the Java Service Provider API to locate classes that
 * implement the ServletContainerInitializer interface. It then instantiates them
 * and calls their onStartup() method. To hook in to Java's SPI loading framework so
 * that the container can call our bootstrapper requires packaging a JAR
 * containing a META-INF/services/javax.servlet.ServletContainerInitializer file that
 * lists the FQN of the concrete classes in the JAR implementing that interface. This
 * JAR then has to be packaged and placed in the web applications /lib directory.
 * <p/>
 * To avoid all the pain of creating and deploying that JAR, the spring-web JAR on the
 * applications class path already provides this via the SpringServletContainerInitializer.
 * The JAR contains the META-INF/services file listing SpringServletContainerInitializer as the
 * concrete class implementing ServletContainerInitializer.
 * <p/>
 * The implementation of SpringServletContainerInitializer declares that it can handle classes
 * of type WebApplicationInitializer. The container scans for classes implementing this
 * and provides them to the onStartup() method, SpringServletContainerInitializer then
 * instantiates them and calls onStartup() on those instances... So, all we have to do
 * is implement WebApplicationInitializer and allow classpath scanning to find us and
 * for SpringServletContainerInitializer to call us!
 * <p/>
 * And so that is how we get to initialise our Spring app contexts, Servlets, Filters etc
 * programmatically at start-up!
 */
@SuppressWarnings("unused")
public class BootstrapFramework implements WebApplicationInitializer
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String DEFAULT_SERVLET_NAME = "default";

	private static final String SERVLET_DISPATCHER_NAME = "springDispatcher";

	static final String[] ADDITIONAL_RESOURCE_PATHS = {"/css/*",
	                                                   "/font-awesome-4.6.3/*",
	                                                   "/sitemap.xml"};

	public void onStartup(ServletContext servletContext) throws ServletException
	{
		LOGGER.info("Configuring Servlet...");

		// All servlets should have access to resources at these paths
		servletContext.getServletRegistration(DEFAULT_SERVLET_NAME).addMapping(ADDITIONAL_RESOURCE_PATHS);

		// Create a root application context from which all sub-contexts inherit. Use annotated classes rather than
		// spring XML context files to configure the application context
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

		// Register the root application context that Spring will instantiate and manage when the application
		// starts up. This will contain beans common to all inheriting application contexts. Also import the
		// security configuration in to the root context so that it applies throughout the application.
		rootContext.register(RootContextConfiguration.class, SecurityConfiguration.class);

		// So that Spring knows when to initialise the application contexts, add a listener to the ServletContext
		// which will notify Springs ContextLoaderListener of Servlet lifecycle events.
		servletContext.addListener(new ContextLoaderListener(rootContext));

		// Create a separate application context to configure the Servlet, again use Spring annotations instead of XML
		AnnotationConfigWebApplicationContext servletContextConfig = new AnnotationConfigWebApplicationContext();

		// Register the class which contains the Servlet configuration
		servletContextConfig.register(ServletContextConfiguration.class);

		// Provide an implementation of an HTTPServlet for the Web container to dispatch requests to. To take advantage
		// of all the features of Spring (ie, MVC), use their implementation of this
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet(SERVLET_DISPATCHER_NAME, new DispatcherServlet(servletContextConfig));

		// Don't wait for the first request to initialise the servlet, initialise on load
		dispatcher.setLoadOnStartup(1);

		// The dispatcher should respond to requests from the root path (Don't use '/*' See p.335 Pro Java For Web Apps)
		dispatcher.addMapping("/");
	}
}
