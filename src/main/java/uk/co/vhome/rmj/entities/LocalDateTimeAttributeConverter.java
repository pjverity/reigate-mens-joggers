package uk.co.vhome.rmj.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Remove this when there is support for Java 8 Date/Times in JPA
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp>
{

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime)
	{
		return (localDateTime == null ? null : Timestamp.valueOf(localDateTime));
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp)
	{
		return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
	}
}
