package uk.co.vhome.clubbed.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.vhome.clubbed.entities.Event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data repository for accessing {@link Event} entities
 */
public interface EventRepository extends JpaRepository<Event, Long>
{
	List<Event> findAllByCompletedFalseOrderByEventDateTime();

	List<Event> findTop10ByCompletedTrueOrderByEventDateTimeDesc();

	List<Event> findAllByCompletedAndEventDateTimeAfterOrEventDateTime(boolean completed,
	                                                                   LocalDateTime afterDateTime,
	                                                                   LocalDateTime dateTime);

	List<Event> findAllByCompletedAndEventDateTimeAfter(boolean completed, LocalDateTime afterDateTime);

	List<Event> findAllByCompletedAndEventDateTimeBeforeOrEventDateTime(boolean completed,
	                                                                    LocalDateTime beforeDateTime,
	                                                                    LocalDateTime dateTime);

	List<Event> findAllByCompletedAndEventDateTimeBefore(boolean completed, LocalDateTime deforeDateTime);
}