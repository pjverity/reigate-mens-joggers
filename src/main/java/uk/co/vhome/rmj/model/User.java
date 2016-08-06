package uk.co.vhome.rmj.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Users")
public class User implements Serializable
{
	private long id;

	private String emailAddress;

	private String password;

	private boolean enabled;

	private Date lastLogin;

	private String role;

	public User()
	{
	}

	public User(String emailAddress, String password)
	{
		this.emailAddress = emailAddress;
		this.password = password;
		this.enabled = true;
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
	public Date getLastLogin()
	{
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin)
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
