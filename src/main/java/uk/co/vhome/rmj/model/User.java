package uk.co.vhome.rmj.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "Users")
public class User implements Serializable
{
	public static final String MEMBER = "MEMBER";

	public static final String ADMIN = "ADMIN";

	private long id;

	private String emailAddress;

	private String firstName;

	private String lastName;

	private String password;

	private boolean enabled;

	private ZonedDateTime lastLogin;

	private String role;

	public User()
	{
	}

	public User(String emailAddress, String password, String firstName, String lastName, String role)
	{
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.enabled = true;
		this.role = role;
		this.lastLogin = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //p.571
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Basic
	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	@Basic
	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	@Basic
	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	@Basic
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@Basic
	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	@Basic
	public ZonedDateTime getLastLogin()
	{
		return lastLogin;
	}

	public void setLastLogin(ZonedDateTime lastLogin)
	{
		this.lastLogin = lastLogin;
	}

	@Basic
	public String getRole()
	{
		return role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

	@Override
	public String toString()
	{
		return "User{" +
				"id=" + id +
				", emailAddress='" + emailAddress + '\'' +
				", enabled=" + enabled +
				", lastLogin=" + lastLogin +
				", role='" + role + '\'' +
				'}';
	}
}
