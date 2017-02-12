package uk.co.vhome.rmj.entities;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

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

	@Column(name = "last_login")
	@Generated(GenerationTime.INSERT)
	private LocalDateTime lastLogin;

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

	public LocalDateTime getLastLogin()
	{
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin)
	{
		this.lastLogin = lastLogin;
	}
}
