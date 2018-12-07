package uk.co.vhome.clubbed.apiobjects;

import java.math.BigDecimal;

public class BalanceUpdatedEvent
{
	private final String username;

	private final String firstName;

	private final Long quantity;

	private final BigDecimal newBalance;

	public BalanceUpdatedEvent(String username, String firstName, Long quantity, BigDecimal newBalance)
	{
		this.username = username;
		this.firstName = firstName;
		this.quantity = quantity;
		this.newBalance = newBalance;
	}

	public String getUsername()
	{
		return username;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public Long getQuantity()
	{
		return quantity;
	}

	public BigDecimal getNewBalance()
	{
		return newBalance;
	}
}
