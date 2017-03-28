package uk.co.vhome.rmj.entities;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

/**
 * Entity representing additional information about users
 */
@Entity
@Table(name = "users")
public class UserDetailsEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	@Column(updatable = false, nullable = false, unique = true)
	private String username;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Basic
	private boolean enabled;

	@Column(name = "last_login")
	@Generated(GenerationTime.INSERT)
	private Instant lastLogin;

	public UserDetailsEntity(String username, String firstName, String lastName)
	{
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public UserDetailsEntity()
	{
	}

	public Long getId()
	{
		return Id;
	}

	public void setId(Long id)
	{
		Id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
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

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public Instant getLastLogin()
	{
		return lastLogin;
	}

	public void setLastLogin(Instant lastLogin)
	{
		this.lastLogin = lastLogin;
	}

	public Date getLastLoginAsDate()
	{
		return Date.from(lastLogin);
	}
}
