package uk.co.vhome.rmj.entities;

import javax.persistence.*;
import java.time.Instant;

/**
 * Entity for persisting token purchases
 */
@Entity
@Table(name = "purchases")
public class Purchase
{
	@Id
	@GeneratedValue
	private Long id;

	@Basic
	@GeneratedValue
	private Instant purchaseTimestamp;

	@Basic
	private String username;

	@Basic
	private int quantity;

	public Purchase()
	{
	}

	public Purchase(String username, int quantity)
	{
		this.username = username;
		this.quantity = quantity;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Instant getPurchaseTimestamp()
	{
		return purchaseTimestamp;
	}

	public void setPurchaseTimestamp(Instant purchaseTimestamp)
	{
		this.purchaseTimestamp = purchaseTimestamp;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
}
