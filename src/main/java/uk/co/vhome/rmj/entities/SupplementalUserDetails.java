package uk.co.vhome.rmj.entities;

import javax.persistence.*;

/**
 * Entity for describing additional information about members
 */
@Entity
@Table(name = "user_details")
public class SupplementalUserDetails
{
	@Id
	@Column(updatable = false, nullable = false, unique = true, name = "username")
	private String emailAddress;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	public SupplementalUserDetails(String userId, String firstName, String lastName)
	{
		this.emailAddress = userId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public SupplementalUserDetails()
	{
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
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
