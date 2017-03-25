package uk.co.vhome.rmj.entities;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Basic(optional = false)
	@Column(name = "purchase_time", nullable = false, updatable = false)
	@Generated(GenerationTime.INSERT)
	private Instant purchaseTime;

	@Basic(optional = false)
	@Column(nullable = false, updatable = false)
	private String username;

	@Basic(optional = false)
	@Column(nullable = false, updatable = false)
	private int quantity;

	public Purchase()
	{
	}

	public Purchase(String username, int quantity)
	{
		this.username = username;
		this.quantity = quantity;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Instant getPurchaseTime()
	{
		return purchaseTime;
	}

	public void setPurchaseTime(Instant purchaseTime)
	{
		this.purchaseTime = purchaseTime;
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

	@Override
	public String toString()
	{
		return "Purchase{" +
				       "id=" + id +
				       ", purchaseTime=" + purchaseTime +
				       ", username='" + username + '\'' +
				       ", quantity=" + quantity +
				       '}';
	}
}
