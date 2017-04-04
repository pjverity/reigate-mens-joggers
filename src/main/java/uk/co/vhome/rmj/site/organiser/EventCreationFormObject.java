package uk.co.vhome.rmj.site.organiser;

import org.springframework.format.annotation.DateTimeFormat;
import uk.co.vhome.rmj.site.form.validation.NotPastLocalDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventCreationFormObject
{
	@NotNull
	@NotPastLocalDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate eventDate;

	@NotNull
	private LocalTime eventTime;

	public LocalDate getEventDate()
	{
		return eventDate;
	}

	public void setEventDate(LocalDate eventDate)
	{
		this.eventDate = eventDate;
	}

	public LocalTime getEventTime()
	{
		return eventTime;
	}

	public void setEventTime(LocalTime eventTime)
	{
		this.eventTime = eventTime;
	}
}
