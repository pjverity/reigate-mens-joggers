package uk.co.vhome.clubbed.mail;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.vhome.clubbed.apiobjects.LowBalanceEvent;
import uk.co.vhome.clubbed.mail.service.MailService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class LowBalanceEmailNotification
{
	private static final String LOW_BALANCE_NOTIFICATION_TEMPLATE = "low-balance-notification.html";

	private static final String TEMPLATE_PARAMETER_FIRST_NAME = "firstName";

	private static final String TEMPLATE_PARAMETER_LAST_NAME = "lastName";

	private static final String TEMPLATE_PARAMETER_QUANTITY = "quantity";

	private static final String TEMPlATE_PARAMETER_BALANCE = "balance";

	private static final Map<String, Object> TEMPLATE_VALUES = new HashMap<>();

	private final MailService mailService;

	@Autowired
	public LowBalanceEmailNotification(MailService mailService)
	{
		this.mailService = mailService;
	}

	@EventHandler
	public void on(LowBalanceEvent lowBalanceEvent)
	{
		populateTemplateValues(lowBalanceEvent.getFirstName(),
		                       lowBalanceEvent.getLastName(),
		                       Math.abs(lowBalanceEvent.getQuantity()),
		                       lowBalanceEvent.getNewBalance());

		mailService.sendEMail(lowBalanceEvent.getUsername(),
		                      "Low balance alert",
		                      LOW_BALANCE_NOTIFICATION_TEMPLATE,
		                      TEMPLATE_VALUES);
	}

	private void populateTemplateValues(String firstName, String lastName, Long quantity, BigDecimal newBalance)
	{
		TEMPLATE_VALUES.put(TEMPLATE_PARAMETER_FIRST_NAME, firstName);
		TEMPLATE_VALUES.put(TEMPLATE_PARAMETER_LAST_NAME, lastName);
		TEMPLATE_VALUES.put(TEMPLATE_PARAMETER_QUANTITY, quantity);
		TEMPLATE_VALUES.put(TEMPlATE_PARAMETER_BALANCE, newBalance);
	}

}
