package uk.co.vhome.rmj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

@Configuration
public class ReigateMensJoggersConfiguration
{
	// Configure our Datasource (ie, the connection to the Database) See p.602
	@Bean
	public DataSource dataSource()
	{
		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		return lookup.getDataSource("jdbc/RMJ");
	}

}
