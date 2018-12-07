package uk.co.vhome.clubbed.apiobjects;

import java.util.Objects;
import java.util.StringJoiner;

public class ClubEnquiryCreatedEvent extends EnquiryEvent
{
	private final String firstName;

	private final String lastName;

	private String phoneNumber;

	public ClubEnquiryCreatedEvent(String emailAddress, String firstName, String lastName, String phoneNumber)
	{
		super(emailAddress);

		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
	}

	public ClubEnquiryCreatedEvent(String emailAddress, String firstName, String lastName)
	{
		this(emailAddress, firstName, lastName, null);
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ClubEnquiryCreatedEvent that = (ClubEnquiryCreatedEvent) o;

		// No two events should have the same e-mail address where any of the other fields differ. If they do,
		// something has gone very wrong! The model (enforced by DB constraints) would prevent the same e-mail
		// address registering twice, so the other fields could not differ.
		return Objects.equals(this.getEmailAddress(), that.getEmailAddress());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getEmailAddress());
	}

	@Override
	public String toString()
	{
		return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
				       .add("emailAddress = " + getEmailAddress())
				       .add("firstName = " + firstName)
				       .add("lastName = " + lastName)
				       .toString();
	}

}
