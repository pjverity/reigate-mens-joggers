package uk.co.vhome.rmj.site.organiser;

import org.springframework.data.geo.Metrics;
import uk.co.vhome.rmj.entities.Event;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Form object for maintaining which members were at an event
 */
public class EventRegistrationFormObject
{
	private List<EventRegistrationFormRow> rows;

	@NotNull
	private Event event;

	@NotNull
	private Double distance;

	@NotNull(message = "{uk.co.vhome.rmj.site.organiser.EventRegistrationFormObject.metric.Null}")
	private Metrics metric;

	public EventRegistrationFormObject()
	{
	}

	public EventRegistrationFormObject(List<EventRegistrationFormRow> rows)
	{
		this.rows = rows;
	}

	public List<EventRegistrationFormRow> getRows()
	{
		return rows;
	}

	public void setRows(List<EventRegistrationFormRow> rows)
	{
		this.rows = rows;
	}

	public Event getEvent()
	{
		return event;
	}

	public void setEvent(Event event)
	{
		this.event = event;
	}

	public Double getDistance()
	{
		return distance;
	}

	public void setDistance(Double distance)
	{
		this.distance = distance;
	}

	public Metrics getMetric()
	{
		return metric;
	}

	public void setMetric(Metrics metric)
	{
		this.metric = metric;
	}
}
