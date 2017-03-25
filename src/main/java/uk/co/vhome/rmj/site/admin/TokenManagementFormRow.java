package uk.co.vhome.rmj.site.admin;

import uk.co.vhome.rmj.entities.MemberBalance;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class TokenManagementFormRow
{
	private MemberBalance memberBalance;

	@NotNull
	@Digits(integer = 2, fraction = 0, message = "Invalid quantity")
	private int quantity;

	public TokenManagementFormRow(MemberBalance memberBalance)
	{
		this.memberBalance = memberBalance;
	}

	public MemberBalance getMemberBalance()
	{
		return memberBalance;
	}

	public void setMemberBalance(MemberBalance memberBalance)
	{
		this.memberBalance = memberBalance;
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
