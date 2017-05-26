package uk.co.vhome.rmj.notifications;

import uk.co.vhome.rmj.entities.UserDetailsEntity;

public class BalanceUpdatedNotification
{
	private final UserDetailsEntity userDetails;

	private final int quantity;

	private final Integer balance;

	public BalanceUpdatedNotification(UserDetailsEntity userDetails, int quantity, Integer balance)
	{
		this.userDetails = userDetails;
		this.quantity = quantity;
		this.balance = balance;
	}

	public UserDetailsEntity getUserDetails()
	{
		return userDetails;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public Integer getBalance()
	{
		return balance;
	}
}
