package uk.co.vhome.clubbed.eventmanagement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import uk.co.vhome.clubbed.entities.Event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;


// Prevent DB migrations from taking place
@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class, showSql = false)
// Change the default 'create-drop' strategy from to avoid a Derby specific issue (https://github.com/spring-projects/spring-boot/issues/7706)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=none")
@Import(DefaultEventManagementService.class)
class EventManagementServiceITCase
{
	private final LocalDateTime EVENT_2016_12_31 = LocalDateTime.of(2016, 12, 31, 13, 25, 0);

	private final LocalDateTime EVENT_2017_01_01 = LocalDateTime.of(2017, 1, 1, 14, 30, 0);

	private final LocalDateTime EVENT_2017_01_02 = LocalDateTime.of(2017, 1, 2, 12, 20, 0);

	private final LocalDateTime EVENT_2017_01_02_MIDNIGHT = LocalDateTime.of(2017, 1, 2, 0, 0, 0);

	private final LocalDateTime EVENT_2017_01_03 = LocalDateTime.of(2017, 1, 3, 12, 0, 0);

	@Autowired
	private EventManagementService eventManagementService;

	@Test
	@DisplayName("Incomplete schedules are returned")
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql"})
	void findsAllUncompletedSchedules()
	{
		List<Event> incompleteEvents = eventManagementService.findAllIncompleteEvents();

		incompleteEvents.forEach(e -> System.out.println(e.getEventDetails().getDistance()));

		assertTrue(!incompleteEvents.isEmpty());
		assertEquals(2, incompleteEvents.size());

		assertEquals(EVENT_2016_12_31, incompleteEvents.get(0).getEventDateTime());
		assertEquals(EVENT_2017_01_01, incompleteEvents.get(1).getEventDateTime());
	}

	@Test
	@DisplayName("Completed schedules are returned")
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql"})
	void findsAllCompletedSchedules()
	{
		List<Event> completedEvents = eventManagementService.findTop10CompletedEvents();

		assertTrue(!completedEvents.isEmpty());
		assertEquals(2, completedEvents.size());

		assertEquals(EVENT_2017_01_02, completedEvents.get(0).getEventDateTime() );
		assertEquals(EVENT_2017_01_02_MIDNIGHT, completedEvents.get(1).getEventDateTime() );
	}

	@Test
	@DisplayName("New event created")
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql"})
	void createsNewEvent()
	{
		eventManagementService.createNewEvent(EVENT_2017_01_03);

		List<Event> incompleteEvents = eventManagementService.findAllIncompleteEvents();

		assertTrue(!incompleteEvents.isEmpty());
		assertEquals(3, incompleteEvents.size());

		assertEquals(EVENT_2016_12_31, incompleteEvents.get(0).getEventDateTime());
		assertEquals(EVENT_2017_01_01, incompleteEvents.get(1).getEventDateTime());
		assertEquals(EVENT_2017_01_03, incompleteEvents.get(2).getEventDateTime());
		assertNull(incompleteEvents.get(2).getEventDetails().getDistance());
	}

	@Test
	@DisplayName("Event completed")
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql"})
	void eventSetAsComplete()
	{
		List<Event> incompleteEvents = eventManagementService.findAllIncompleteEvents();

		// Choose an event to complete
		Event eventToComplete = incompleteEvents.get(0);
		eventToComplete.getEventDetails().setDistance(BigDecimal.valueOf(2.5));

		// Mark it completed
		eventManagementService.completeEvent(eventToComplete);

		// Ensure the number of completed events has increased
		List<Event> completedEvents = eventManagementService.findTop10CompletedEvents();
		assertEquals(3, completedEvents.size());

		// Ensure the number of incomplete events has decreased
		incompleteEvents = eventManagementService.findAllIncompleteEvents();
		assertEquals(1, incompleteEvents.size());

		// Find the completed event and ensure the distance has been updated
		Optional<Event> completedEvent = completedEvents.stream().filter(Predicate.isEqual(eventToComplete)).findFirst();
		assertTrue(completedEvent.isPresent());
		assertEquals(BigDecimal.valueOf(2.5), completedEvent.get().getEventDetails().getDistance());
	}
	
	@Test
	@DisplayName("Event cancelled")
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql"})
	void eventCancelled()
	{
		List<Event> incompleteEvents = eventManagementService.findAllIncompleteEvents();

		Event eventToCancel = incompleteEvents.get(0);

		eventManagementService.cancelEvent(eventToCancel);

		incompleteEvents = eventManagementService.findAllIncompleteEvents();
		assertEquals(1, incompleteEvents.size());

		assertNotEquals(incompleteEvents.get(0).getId(), eventToCancel.getId());
	}

	@Test
	@DisplayName("Exception thrown when attempting to complete a cancelled event")
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql"})
	void throwsExceptionWhenCompletedEventIsCancelled()
	{
		List<Event> completedEvents = eventManagementService.findTop10CompletedEvents();

		Event eventToCancel = completedEvents.get(0);

		assertThrows(IllegalArgumentException.class, () -> eventManagementService.cancelEvent(eventToCancel));
	}

	@Test
	@DisplayName("Exception thrown when creating a duplicate event")
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql"})
	void throwsExceptionWhenDuplicateEventCreated()
	{
		assertThrows(DataIntegrityViolationException.class, () -> eventManagementService.createNewEvent(EVENT_2016_12_31));
	}

	@Test
	@DisplayName("Returned events on or before a given time")
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql"})
	void fetchesEventOnOrBeforeDate()
	{
		List<Event> events = eventManagementService.fetchEventsBefore(EVENT_2017_01_01, true, false);

		assertEquals(2, events.size(), "Should be two events");
	}

	@Test
	@DisplayName("Returned events before a given time")
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql"})
	void fetchesEventBeforeDate()
	{
		List<Event> events = eventManagementService.fetchEventsBefore(EVENT_2017_01_01, false, false);

		assertEquals(1, events.size(), "Should be one events");
	}

	@Test
	@DisplayName("Returned events on or after a given time")
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql"})
	void fetchesEventOnOrAfterDate()
	{
		List<Event> events = eventManagementService.fetchEventsAfter(EVENT_2017_01_02_MIDNIGHT, true, true);

		assertEquals(2, events.size(), "Should be two events");
	}

	@Test
	@DisplayName("Returned events after a given time")
	@Sql({"/user-schema.sql", "/test-users.sql", "/events-schema.sql", "/events-data.sql"})
	void fetchesEventAfterDate()
	{
		List<Event> events = eventManagementService.fetchEventsAfter(EVENT_2017_01_02_MIDNIGHT, false, true);

		assertEquals(1, events.size(), "Should be one events");
	}

}