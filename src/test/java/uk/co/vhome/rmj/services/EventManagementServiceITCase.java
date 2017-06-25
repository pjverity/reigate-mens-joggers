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
import uk.co.vhome.rmj.services.core.DefaultEventManagementService;
import uk.co.vhome.rmj.services.core.EventManagementService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@ActiveProfiles({"integration-test"})
@ContextConfiguration(classes = {IntegrationTestConfiguration.class, DefaultEventManagementService.class})
@Transactional
public class EventManagementServiceITCase
{
	private final LocalDateTime EVENT_2016_12_31 = LocalDateTime.of(2016, 12, 31, 13, 25, 0);

	private final LocalDateTime EVENT_2017_01_01 = LocalDateTime.of(2017, 1, 1, 14, 30, 0);

	private final LocalDateTime EVENT_2017_01_02 = LocalDateTime.of(2017, 1, 2, 12, 20, 0);

	private final LocalDateTime EVENT_2017_01_02_MIDNIGHT = LocalDateTime.of(2017, 1, 2, 0, 0, 0);

	private final LocalDateTime EVENT_2017_01_03 = LocalDateTime.of(2017, 1, 3, 12, 0, 0);

	@Inject
	private EventManagementService eventManagementService;

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void findsAllUncompletedSchedules() throws Exception
	{
		List<Event> incompleteEvents = eventManagementService.findAllIncompleteEvents();

		incompleteEvents.forEach(e -> System.out.println(e.getEventInfo().getDistance()));

		assertTrue(!incompleteEvents.isEmpty());
		assertEquals(2, incompleteEvents.size());

		assertEquals(EVENT_2016_12_31, incompleteEvents.get(0).getEventDateTime());
		assertEquals(EVENT_2017_01_01, incompleteEvents.get(1).getEventDateTime());
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void findsAllCompletedSchedules() throws Exception
	{
		List<Event> completedEvents = eventManagementService.findTop10CompletedEvents();

		assertTrue(!completedEvents.isEmpty());
		assertEquals(2, completedEvents.size());

		assertEquals(EVENT_2017_01_02, completedEvents.get(0).getEventDateTime() );
		assertEquals(EVENT_2017_01_02_MIDNIGHT, completedEvents.get(1).getEventDateTime() );
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void createsNewEvent() throws Exception
	{
		eventManagementService.createNewEvent(EVENT_2017_01_03);

		List<Event> incompleteEvents = eventManagementService.findAllIncompleteEvents();

		assertTrue(!incompleteEvents.isEmpty());
		assertEquals(3, incompleteEvents.size());

		assertEquals(EVENT_2016_12_31, incompleteEvents.get(0).getEventDateTime());
		assertEquals(EVENT_2017_01_01, incompleteEvents.get(1).getEventDateTime());
		assertEquals(EVENT_2017_01_03, incompleteEvents.get(2).getEventDateTime());
		assertEquals(null, incompleteEvents.get(2).getEventInfo().getDistance());
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
		assertEquals(3, completedEvents.size());

		// Ensure the number of incomplete events has decreased
		incompleteEvents = eventManagementService.findAllIncompleteEvents();
		assertEquals(1, incompleteEvents.size());

		// Find the completed event and ensure the distance has been updated
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
		eventManagementService.createNewEvent(EVENT_2016_12_31);
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void fetchesEventOnOrBeforeDate() throws Exception
	{
		List<Event> events = eventManagementService.fetchEventsBefore(EVENT_2017_01_01, true, false);

		assertEquals(2, events.size(), "Should be two events");
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void fetchesEventBeforeDate() throws Exception
	{
		List<Event> events = eventManagementService.fetchEventsBefore(EVENT_2017_01_01, false, false);

		assertEquals(1, events.size(), "Should be one events");
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void fetchesEventOnOrAfterDate() throws Exception
	{
		List<Event> events = eventManagementService.fetchEventsAfter(EVENT_2017_01_02_MIDNIGHT, true, true);

		assertEquals(2, events.size(), "Should be two events");
	}

	@Test
	@Sql({"/schema.sql", "/data.sql"})
	public void fetchesEventAfterDate() throws Exception
	{
		List<Event> events = eventManagementService.fetchEventsAfter(EVENT_2017_01_02_MIDNIGHT, false, true);

		assertEquals(1, events.size(), "Should be one events");
	}

}