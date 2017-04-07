package uk.co.vhome.rmj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.vhome.rmj.entities.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>
{
	List<Event> findAllByCompletedFalseOrderByEventDateTime();

	List<Event> findTop10ByCompletedTrueOrderByEventDateTimeDesc();
}
