package uk.co.vhome.rmj.config;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.util.Hashtable;
import java.util.Map;

@Configuration
@ComponentScan(
		basePackages = "uk.co.vhome",
		excludeFilters = @ComponentScan.Filter(Controller.class)
)
@EnableTransactionManagement(mode = AdviceMode.PROXY) //p.607
public class RootContextConfiguration implements TransactionManagementConfigurer
{

	private static final String HIBERNATE_DIALECT = "org.hibernate.dialect.PostgreSQL94Dialect";

	private static final String SCHEMA_GENERATION_KEY = "javax.persistence-schema-generation.database.action";

	// Configure our Datasource (ie, the connection to the Database) See p.602
	@Bean
	public DataSource springJpaDataSource()
	{
		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		return lookup.getDataSource("jdbc/RMJ");
	}

	// Configure the persistence unit (Which managers our entities and configures the JPA implementation
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
		factoryBean.setDataSource(springJpaDataSource());
		factoryBean.setPackagesToScan("uk.co.vhome.rmj");
		factoryBean.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
		factoryBean.setValidationMode(ValidationMode.NONE);
		factoryBean.setJpaPropertyMap(properties);
		return factoryBean;
	}

	@Bean
	public PlatformTransactionManager jpaTransactionManager()
	{
		return new JpaTransactionManager(entityManagerFactoryBean().getObject());
	}

	// Protect against Spring choosing the wrong transaction manager is we create several (p.609)
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager()
	{
		return jpaTransactionManager();
	}
}
