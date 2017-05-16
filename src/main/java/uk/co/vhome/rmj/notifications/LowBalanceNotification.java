package uk.co.vhome.rmj.notifications;

import uk.co.vhome.rmj.entities.UserDetailsEntity;

public class LowBalanceNotification
{
	private final UserDetailsEntity userDetails;

	private final int quantity;

	private final int currentBalance;

	public LowBalanceNotification(UserDetailsEntity userDetails, int quantity, int currentBalance)
	{
		this.userDetails = userDetails;
		this.quantity = quantity;
		this.currentBalance = currentBalance;
	}

	public UserDetailsEntity getUserDetails()
	{
		return userDetails;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public int getCurrentBalance()
	{
		return currentBalance;
	}
}
