package uk.co.vhome.rmj.site.organiser;

import uk.co.vhome.clubbed.domainobjects.entities.Event;

import java.util.List;

public class EventCancellationFormObject
{
	private List<Event> events;

	public List<Event> getEvents()
	{
		return events;
	}

	public void setEvents(List<Event> events)
	{
		this.events = events;
	}
}
