package uk.co.vhome.rmj.entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * Entity representing user credentials and state
 */
@Entity
@Table(name = "users")
public class User
{
	private Long id;

	private String username;

	private String password;

	private boolean enabled;

	private UserDetail userDetail;

	@Id
	@SequenceGenerator(name="users_id_seq", sequenceName="users_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="users_id_seq")
	@Column(updatable = false)
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

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	@OneToOne
	@MapsId
	@JoinColumn(name = "id")
	public UserDetail getUserDetail()
	{
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail)
	{
		this.userDetail = userDetail;
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
		User that = (User) o;
		return enabled == that.enabled &&
				Objects.equals(id, that.id) &&
				Objects.equals(username, that.username) &&
				Objects.equals(password, that.password);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, username, password, enabled);
	}
}
