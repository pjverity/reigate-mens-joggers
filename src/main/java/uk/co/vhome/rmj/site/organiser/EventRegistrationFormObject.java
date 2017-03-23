package uk.co.vhome.rmj.site.organiser;

import java.util.List;

/**
 * Form object for maintaining which members were at an event
 */
public class EventRegistrationFormObject
{
	private List<EventRegistrationFormRow> rows;

	public List<EventRegistrationFormRow> getRows()
	{
		return rows;
	}

	public void setRows(List<EventRegistrationFormRow> rows)
	{
		this.rows = rows;
	}

}
