package uk.co.vhome.rmj.entities;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

/**
 * Entity representing additional information about users
 */
@Entity
@Table(name = "users")
public class UserDetailsEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	@ManyToMany(mappedBy = "userDetailsEntities")
	private Set<Event> events;

	public UserDetailsEntity(Long userId, String username, boolean enabled)
	{
		this.id = userId;
		this.username = username;
		this.enabled = enabled;
	}

	public UserDetailsEntity()
	{
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
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

	public Set<Event> getEvents()
	{
		return events;
	}

	public void setEvents(Set<Event> events)
	{
		this.events = events;
	}

	public Date getLastLoginAsDate()
	{
		return Date.from(lastLogin);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		UserDetailsEntity that = (UserDetailsEntity) o;

		return id != null ? id.equals(that.id) : that.id == null;
	}

	@Override
	public int hashCode()
	{
		return id != null ? id.hashCode() : 0;
	}
}
