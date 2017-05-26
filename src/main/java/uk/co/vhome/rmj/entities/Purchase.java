package uk.co.vhome.rmj.entities;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.Instant;

/**
 * Entity for persisting token purchases
 */
@Entity
@Table(name = "purchases", indexes = @Index(columnList = "users_id"))
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
	@Column(name = "users_id", nullable = false, updatable = false)
	private Long userId;

	@Basic(optional = false)
	@Column(nullable = false, updatable = false)
	private int quantity;

	public Purchase()
	{
	}

	public Purchase(Long userId, int quantity)
	{
		this.userId = userId;
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

	public Long getUsername()
	{
		return userId;
	}

	public void setUsername(Long userId)
	{
		this.userId = userId;
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
				       ", userId='" + userId + '\'' +
				       ", quantity=" + quantity +
				       '}';
	}
}
