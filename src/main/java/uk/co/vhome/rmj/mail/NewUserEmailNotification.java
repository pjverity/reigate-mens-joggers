package uk.co.vhome.rmj.mail;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import uk.co.vhome.clubbed.apiobjects.NewUserEvent;
import uk.co.vhome.rmj.mail.service.MailService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Component
public class NewUserEmailNotification
{
	private static final String REGISTRATION_CONFIRMATION_TEMPLATE = "registration-confirmation.html";

	private static final String TEMPLATE_PARAMETER_FIRST_NAME = "firstName";

	private static final Map<String, Object> TEMPLATE_VALUES = new HashMap<>();

	private MailService mailService;

	@Inject
	public NewUserEmailNotification(MailService mailService)
	{
		this.mailService = mailService;
	}

	@EventHandler
	public void on(NewUserEvent newUserEvent)
	{
		populateTemplateValues(newUserEvent.getFirstName());

		mailService.sendEMailAndBccAdmin(newUserEvent.getUsername(),
		                                 "Welcome to Reigate Men's Joggers!",
		                                 REGISTRATION_CONFIRMATION_TEMPLATE,
		                                 TEMPLATE_VALUES);
	}

	private void populateTemplateValues(String firstName)
	{
		TEMPLATE_VALUES.put(TEMPLATE_PARAMETER_FIRST_NAME, firstName);
	}

}