package uk.co.vhome.rmj.site.organiser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import uk.co.vhome.rmj.services.EventManagementService;
import uk.co.vhome.rmj.site.ViewControllerTestConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ViewControllerTestConfiguration.class})
public class EventSchedulingViewControllerTest
{

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Mock
	private EventManagementService mockEventManagementService;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		EventSchedulingViewController eventSchedulingViewController = new EventSchedulingViewController(mockEventManagementService);

		this.mockMvc = MockMvcBuilders.standaloneSetup(eventSchedulingViewController).build();
	}

	@Test
	public void allowsCreationOfPastEvent() throws Exception
	{
		LocalDate eventDate = LocalDate.of(2017, 1, 1);
		LocalTime eventTime = LocalTime.of(12, 30, 0);

		MvcResult mvcResult = mockMvc.perform(post("/organiser/create-event")
				                                      .param("eventDate", "2017-01-01")
				                                      .param("eventHour", "12")
				                                      .param("eventMinutes", "30"))
				                      .andExpect(status().is3xxRedirection())
				                      .andReturn();

		ModelAndView modelAndView = mvcResult.getModelAndView();

		BindingResult bindingResult = BindingResultUtils.getBindingResult(modelAndView.getModel(), "eventCreationFormObject");

		assertEquals(bindingResult.hasErrors(), false);

		verify(mockEventManagementService).createNewEvent(LocalDateTime.of(eventDate, eventTime));
	}
}
