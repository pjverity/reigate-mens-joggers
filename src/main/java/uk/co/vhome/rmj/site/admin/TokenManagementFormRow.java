package uk.co.vhome.rmj.site.admin;

import uk.co.vhome.rmj.entities.MemberBalance;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class TokenManagementFormRow
{
	private MemberBalance memberBalance;

	@Min(value = 1, message = "{validation.constraint.Min.creditAmount}")
	@Max(value = 20, message = "{validation.constraint.Max.creditAmount}")
	private Integer quantity;

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

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

}
