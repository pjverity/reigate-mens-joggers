package uk.co.vhome.clubbed;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The bootstrap admin user for a new site installation
 */
@Component
public class InitialSiteUser
{
	private String id;

	private String firstName;

	private String lastName;

	private String password;

	public InitialSiteUser(@Value("${initial-site-user.id}") String id,
	                       @Value("${initial-site-user.first-name}") String firstName,
	                       @Value("${initial-site-user.last-name}") String lastName,
	                       @Value("${initial-site-user.password}") String password)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
