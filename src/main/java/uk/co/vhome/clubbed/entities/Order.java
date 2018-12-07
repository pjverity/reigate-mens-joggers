package uk.co.vhome.clubbed.entities;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entity for persisting token purchases
 */
@Entity
@Table(name = "orders", indexes = @Index(columnList = "users_id"))
public class Order
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
	@Column(name = "unit_cost", nullable = false, updatable = false)
	private BigDecimal unitCost;

	@Basic(optional = false)
	@Column(nullable = false, updatable = false)
	private Long quantity;

	protected Order()
	{
	}

	public Order(Long userId, BigDecimal unitCost, Long quantity)
	{
		this.userId = userId;
		this.unitCost = unitCost;
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

	public BigDecimal getUnitCost()
	{
		return unitCost;
	}

	public void setUnitCost(BigDecimal unitCost)
	{
		this.unitCost = unitCost;
	}

	public Long getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Long quantity)
	{
		this.quantity = quantity;
	}

	@Override
	public String toString()
	{
		return "Order{" +
				       "id=" + id +
				       ", purchaseTime=" + purchaseTime +
				       ", userId='" + userId + '\'' +
				       ", unitCost=" + unitCost +
				       ", quantity=" + quantity +
				       '}';
	}
}
