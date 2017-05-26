package uk.co.vhome.rmj.site.organiser;

import org.springframework.format.annotation.DateTimeFormat;
import uk.co.vhome.rmj.site.form.validation.NotPastLocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class EventCreationFormObject
{
	@NotNull
	@NotPastLocalDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate eventDate;

	@NotNull
	@Min(0)
	@Max(23)
	private Integer eventHour;

	@Min(0)
	@Max(60)
	private Integer eventMinutes;

	public LocalDate getEventDate()
	{
		return eventDate;
	}

	public void setEventDate(LocalDate eventDate)
	{
		this.eventDate = eventDate;
	}

	public Integer getEventHour()
	{
		return eventHour;
	}

	public void setEventHour(Integer eventHour)
	{
		this.eventHour = eventHour;
	}

	public Integer getEventMinutes()
	{
		return eventMinutes;
	}

	public void setEventMinutes(Integer eventMinutes)
	{
		this.eventMinutes = eventMinutes;
	}

}
