package uk.co.vhome.rmj.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Entity
@Table(name = "events")
public class Event
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "event_datetime")
	private LocalDateTime eventDateTime;

	@Basic
	private boolean completed;

	private transient boolean cancelled;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public LocalDateTime getEventDateTime()
	{
		return eventDateTime;
	}

	public void setEventDateTime(LocalDateTime eventDateTime)
	{
		this.eventDateTime = eventDateTime;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}

	public boolean isCancelled()
	{
		return cancelled;
	}

	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}

	public String getEventDateTimeText()
	{
		return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT).format(eventDateTime);
	}
}
