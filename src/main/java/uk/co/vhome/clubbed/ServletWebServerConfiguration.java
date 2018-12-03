package uk.co.vhome.clubbed;

import org.apache.catalina.Context;
import org.apache.catalina.webresources.DirResourceSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletWebServerConfiguration
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ServletWebServerConfiguration.class);

	@Bean
	public TomcatServletWebServerFactory servletWebServerFactory(@Value("${tomcat.context.pre-resource.base}") String base)
	{
		return new TomcatServletWebServerFactory()
		{
			@Override
			protected void configureContext(Context context, ServletContextInitializer[] initializers)
			{
				super.configureContext(context, initializers);

				DirResourceSet webResourceSet = new DirResourceSet();
				webResourceSet.setBase(base);
				webResourceSet.setInternalPath("/images");
				webResourceSet.setWebAppMount("/images");

				LOGGER.debug("Resources is null: " + (context.getResources() == null ? "true" : "false"));

				context.getResources().addPreResources(webResourceSet);
			}

		};
	}

}
