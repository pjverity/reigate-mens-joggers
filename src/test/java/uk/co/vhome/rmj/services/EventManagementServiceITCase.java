package uk.co.vhome.rmj.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.IntegrationTestConfiguration;
import uk.co.vhome.rmj.entities.Event;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles({"integration-test"})
@ContextConfiguration(classes = {IntegrationTestConfiguration.class, DefaultEventManagementService.class})
@Transactional
public class EventManagementServiceITCase
{
	private final LocalDateTime TEST_EVENT_DATETIME1 = LocalDateTime.of(2017, 7, 14, 13, 25, 0);

	private final LocalDateTime TEST_EVENT_DATETIME2 = LocalDateTime.of(2017, 8, 11, 14, 30, 0);

	private final LocalDateTime TEST_EVENT_DATETIME3 = LocalDateTime.of(2017, 8, 31, 12, 20, 0);

	@Inject
	private EventManagementService eventManagementService;

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void findsAllUncompletedSchedules() throws Exception
	{
		List<Event> incompleteEvents = eventManagementService.findAllIncompleteEvents();

		incompleteEvents.forEach(e -> System.out.println(e.getEventInfo().getDistance()));

		assertNotNull(incompleteEvents);
		assertEquals(2, incompleteEvents.size());

		assertEquals(TEST_EVENT_DATETIME1, incompleteEvents.get(0).getEventDateTime());
		assertEquals(TEST_EVENT_DATETIME2, incompleteEvents.get(1).getEventDateTime());
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void findsAllCompletedSchedules() throws Exception
	{
		List<Event> completedEvents = eventManagementService.findTop10CompletedEvents();

		assertNotNull(completedEvents);
		assertEquals(1, completedEvents.size());

		assertEquals(TEST_EVENT_DATETIME3, completedEvents.get(0).getEventDateTime() );
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void createsNewEvent() throws Exception
	{
		LocalDateTime eventDateTime = LocalDateTime.of(2017, 6, 30, 17, 30);
		eventManagementService.createNewEvent(eventDateTime);

		List<Event> incompleteEvents = eventManagementService.findAllIncompleteEvents();

		assertNotNull(incompleteEvents);
		assertEquals(3, incompleteEvents.size());

		assertEquals(eventDateTime, incompleteEvents.get(0).getEventDateTime());
		assertEquals(null, incompleteEvents.get(0).getEventInfo().getDistance());
		assertEquals(TEST_EVENT_DATETIME1, incompleteEvents.get(1).getEventDateTime());
		assertEquals(TEST_EVENT_DATETIME2, incompleteEvents.get(2).getEventDateTime());
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void eventSetAsComplete() throws Exception
	{
		List<Event> incompleteEvents = eventManagementService.findAllIncompleteEvents();

		// Choose an event to complete
		Event eventToComplete = incompleteEvents.get(0);
		eventToComplete.getEventInfo().setDistance(BigDecimal.valueOf(2.5));

		// Mark it completed
		eventManagementService.completeEvent(eventToComplete);

		// Ensure the number of completed events has increased
		List<Event> completedEvents = eventManagementService.findTop10CompletedEvents();
		assertEquals(2, completedEvents.size());

		// Ensure the number of incomplete events has decreased
		incompleteEvents = eventManagementService.findAllIncompleteEvents();
		assertEquals(1, incompleteEvents.size());

		// Find out completed event and ensure the distance has been updated
		Optional<Event> completedEvent = completedEvents.stream().filter(Predicate.isEqual(eventToComplete)).findFirst();
		assertEquals(true, completedEvent.isPresent());
		assertEquals(BigDecimal.valueOf(2.5), completedEvent.get().getEventInfo().getDistance());
	}
	
	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void eventCancelled() throws Exception
	{
		List<Event> incompleteEvents = eventManagementService.findAllIncompleteEvents();

		Event eventToCancel = incompleteEvents.get(0);

		eventManagementService.cancelEvent(eventToCancel);

		incompleteEvents = eventManagementService.findAllIncompleteEvents();
		assertEquals(1, incompleteEvents.size());

		assertNotEquals(incompleteEvents.get(0).getId(), eventToCancel.getId());
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void eventsBeforeDate() throws Exception
	{
		List<Event> events = eventManagementService.fetchEventsBefore(TEST_EVENT_DATETIME1, false, false);
		assertEquals(0, events.size());

		events = eventManagementService.fetchEventsBefore(TEST_EVENT_DATETIME1, true, false);
		assertEquals(1, events.size());

		events = eventManagementService.fetchEventsBefore(TEST_EVENT_DATETIME3, true, true);
		assertEquals(1, events.size());
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void eventsAfterDate() throws Exception
	{
		List<Event> events = eventManagementService.fetchEventsAfter(TEST_EVENT_DATETIME1, false, false);
		assertEquals(1, events.size());

		events = eventManagementService.fetchEventsAfter(TEST_EVENT_DATETIME1, true, false);
		assertEquals(2, events.size());

		events = eventManagementService.fetchEventsAfter(TEST_EVENT_DATETIME3, true, true);
		assertEquals(1, events.size());
	}

	@Test(expected = IllegalArgumentException.class)
	@Sql({"/schema.sql", "/data.sql"})
	public void throwsExceptionWhenCompletedEventIsCancelled() throws Exception
	{
		List<Event> completedEvents = eventManagementService.findTop10CompletedEvents();

		Event eventToCancel = completedEvents.get(0);

		eventManagementService.cancelEvent(eventToCancel);
	}

	@Test(expected = DataIntegrityViolationException.class)
	@Sql({"/schema.sql", "/data.sql"})
	public void throwsExceptionWhenDuplicateEventCreated() throws Exception
	{
		eventManagementService.createNewEvent(TEST_EVENT_DATETIME1);
	}

}