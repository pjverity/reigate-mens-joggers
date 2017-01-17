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
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Type(type="pg-uuid")
	private UUID uuid;

	@Column(name = "user_id")
	private String userId;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	public Registration(String userId)
	{
		this.userId = userId;
	}

	public Registration()
	{
		this(null);
	}

}
