package uk.co.vhome.rmj.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Remove this when there is support for Java 8 Date/Times in JPA
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date>
{

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate)
	{
		return (localDate == null ? null : Date.valueOf(localDate));
	}

	@Override
	public LocalDate convertToEntityAttribute(Date sqlDate)
	{
		return (sqlDate == null ? null : sqlDate.toLocalDate());
	}
}