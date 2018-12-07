package uk.co.vhome.clubbed.entities;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "users")
public class UserEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "userEntity", orphanRemoval = true)
	private UserDetailsEntity userDetailsEntity;

	@Column(updatable = false, nullable = false, unique = true)
	private String username;

	@Basic
	private boolean enabled;

	@Column(name = "last_login")
	@Generated(GenerationTime.INSERT)
	private Instant lastLogin;

	protected UserEntity()
	{
	}

	public UserEntity(Long userId, String username, boolean enabled)
	{
		this.id = userId;
		this.username = username;
		this.enabled = enabled;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public UserDetailsEntity getUserDetailsEntity()
	{
		return userDetailsEntity;
	}

	public void setUserDetailsEntity(UserDetailsEntity userDetailsEntity)
	{
		this.userDetailsEntity = userDetailsEntity;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
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

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserEntity that = (UserEntity) o;

		return Objects.equals(this.enabled, that.enabled) &&
					   // TODO - Does this need fixing? Can be called outside of a transaction and fails
//				       Objects.equals(this.events, that.events) &&
				       Objects.equals(this.id, that.id) &&
				       Objects.equals(this.lastLogin, that.lastLogin) &&
				       Objects.equals(this.username, that.username);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(enabled, /*events,*/ id, lastLogin, username);
	}

}
