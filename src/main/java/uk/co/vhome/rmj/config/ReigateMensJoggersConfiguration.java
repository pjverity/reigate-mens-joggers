package uk.co.vhome.rmj.config;

import org.axonframework.spring.config.EnableAxon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import javax.mail.Session;
import javax.sql.DataSource;

@Configuration
@EnableAxon
public class ReigateMensJoggersConfiguration
{
	// Configure our Datasource (ie, the connection to the Database) See p.602
	@Bean
	public DataSource dataSource()
	{
		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		return lookup.getDataSource("jdbc/RMJ");
	}

	@Bean
	public JavaMailSender jndiJavaMailSender()
	{
		SimpleJndiBeanFactory locator = new SimpleJndiBeanFactory();
		Session session = ((Session) locator.getBean("mail/Session"));

		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setSession(session);
		javaMailSender.setDefaultEncoding("UTF-8");

		return javaMailSender;
	}

	@Bean
	public FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean()
	{
		FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
		bean.setTemplateLoaderPath("/WEB-INF/fmtemplates");
		return bean;
	}
}
