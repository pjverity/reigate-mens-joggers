package uk.co.vhome.clubbed.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "event_details")
public class EventDetails
{
	@Id
	@Column(name = "events_id")
	private Long eventId;

	@OneToOne
	@MapsId
	@JoinColumn(name = "events_id")
	private Event event;

	@Basic
	private BigDecimal distance;

	public Long getEventId()
	{
		return eventId;
	}

	public void setEventId(Long eventId)
	{
		this.eventId = eventId;
	}

	public Event getEvent()
	{
		return event;
	}

	public void setEvent(Event event)
	{
		this.event = event;
	}

	public BigDecimal getDistance()
	{
		return distance;
	}

	public void setDistance(BigDecimal distance)
	{
		this.distance = distance;
	}
}
