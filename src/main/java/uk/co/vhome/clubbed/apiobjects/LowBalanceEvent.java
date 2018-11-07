package uk.co.vhome.clubbed.apiobjects;

import java.math.BigDecimal;

public class LowBalanceEvent
{
	private final String username;

	private final String firstName;

	private final String lastName;

	private final Long quantity;

	private final BigDecimal newBalance;

	public LowBalanceEvent(String username, String firstName, String lastName, Long quantity, BigDecimal newBalance)
	{
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
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

	public String getLastName()
	{
		return lastName;
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
