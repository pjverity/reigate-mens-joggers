package uk.co.vhome.rmj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.vhome.rmj.entities.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>
{
	List<Event> findAllByCompletedFalseOrderByEventDateTime();

	List<Event> findTop10ByCompletedTrueOrderByEventDateTimeDesc();

	List<Event> findAllByCompletedAndEventDateTimeAfter(boolean completed, LocalDateTime beforeDateTime);

	List<Event> findAllByCompletedAndEventDateTimeBefore(boolean completed, LocalDateTime beforeDateTime);

}
