package uk.co.vhome.rmj.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity for describing additional information about members
 */
@Entity
@Table(name = "user_details")
public class UserDetail
{
	@Id
	@Column(name = "user_id")
	private String userId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
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

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
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
}
