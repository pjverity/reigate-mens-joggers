package uk.co.vhome.clubbed.web.site.organiser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.vhome.clubbed.eventmanagement.EventManagementService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventSchedulingViewController.class,
		excludeAutoConfiguration = FlywayAutoConfiguration.class,
		secure = false) // Disable CSRF protection on POST's
@AutoConfigureDataJdbc
class EventSchedulingViewControllerTest
{

	@MockBean
	private EventManagementService mockEventManagementService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void allowsCreationOfPastEvent() throws Exception
	{
		 /*MvcResult mvcResult =*/ mockMvc.perform(post("/organiser/create-event")
				                                      .param("eventDate", "2017-01-01")
				                                      .param("eventHour", "12")
				                                      .param("eventMinutes", "30"))
				                      .andExpect(status().is3xxRedirection())
				                      .andReturn();

//		ModelAndView modelAndView = mvcResult.getModelAndView();

//		BindingResult bindingResult = BindingResultUtils.getBindingResult(modelAndView.getModel(), "eventCreationFormObject");

//		assertEquals(bindingResult.hasErrors(), false);

		LocalDate eventDate = LocalDate.of(2017, 1, 1);
		LocalTime eventTime = LocalTime.of(12, 30, 0);

		verify(mockEventManagementService).createNewEvent(LocalDateTime.of(eventDate, eventTime));
	}
}
