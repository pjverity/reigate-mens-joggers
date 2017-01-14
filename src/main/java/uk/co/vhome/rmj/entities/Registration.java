package uk.co.vhome.rmj.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

/**
 * Entity used for managing new registration requests. The a new UUID will
 * be generated for each new registration request. The user will then
 * confirm or rescind the registation request by that UUID
 */
@Entity
@Table(name = "registrations")
public class Registration
{

	private String username;

	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Type(type="pg-uuid")
	@Id
	private UUID uuid;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	public Registration(String username)
	{
		this.username = username;
	}

	public Registration()
	{
		this(null);
	}

}
