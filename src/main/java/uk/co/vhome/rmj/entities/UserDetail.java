package uk.co.vhome.rmj.entities;

import javax.persistence.*;

/**
 * Entity for describing additional information about members
 */
@Entity
@Table(name = "user_details")
public class UserDetail
{
	@Id
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	public UserDetail(Long id, String firstName, String lastName)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public UserDetail()
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
