package uk.co.vhome.rmj.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by paulverity on 16/01/2017.
 */
@Entity
@Table(name = "user_details")
public class UserDetail
{
	private String userId;

	private String firstName;

	private String lastName;

	public UserDetail(String userId, String firstName, String lastName)
	{
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public UserDetail()
	{
	}

	@Id
	@Column(name = "user_id")
	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String username)
	{
		this.userId = username;
	}

	@Column(name = "last_name")
	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	@Column(name = "first_name")
	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
}
