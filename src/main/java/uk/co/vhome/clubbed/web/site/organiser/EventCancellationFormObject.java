package uk.co.vhome.clubbed.web.site.organiser;

import uk.co.vhome.clubbed.entities.Event;

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
