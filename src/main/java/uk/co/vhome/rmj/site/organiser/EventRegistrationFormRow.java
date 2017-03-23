package uk.co.vhome.rmj.site.organiser;

import uk.co.vhome.rmj.entities.MemberBalance;

/**
 * Represents a row in the event registration table for registering the presence of a member
 */
public class EventRegistrationFormRow
{
	private MemberBalance memberBalance;

	private boolean present;

	public EventRegistrationFormRow(MemberBalance memberBalance)
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

	public boolean isPresent()
	{
		return present;
	}

	public void setPresent(boolean present)
	{
		this.present = present;
	}
}
