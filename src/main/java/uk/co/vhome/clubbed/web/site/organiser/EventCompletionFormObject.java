package uk.co.vhome.clubbed.web.site.organiser;

import org.springframework.data.geo.Metrics;
import uk.co.vhome.clubbed.entities.Event;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Form object for maintaining which members were at an event
 */
public class EventCompletionFormObject
{
	private List<EventCompletionFormRow> rows;

	@NotNull
	private Event event;

	@NotNull
	@Min(1)
	@Max(100)
	private Double distance;

	@NotNull(message = "{EventCompletionFormObject.metric.Null}")
	private Metrics metric;

	public EventCompletionFormObject()
	{
	}

	public EventCompletionFormObject(List<EventCompletionFormRow> rows)
	{
		this.rows = rows;
	}

	public List<EventCompletionFormRow> getRows()
	{
		return rows;
	}

	public void setRows(List<EventCompletionFormRow> rows)
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
