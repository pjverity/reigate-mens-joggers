package uk.co.vhome.clubbed.mail;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.vhome.clubbed.apiobjects.BalanceUpdatedEvent;
import uk.co.vhome.clubbed.mail.service.MailService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class BalanceUpdatedEmailNotification
{
	private static final String BALANCE_UPDATE_NOTIFICATION_TEMPLATE = "balance-update-notification.html";

	private static final String TEMPLATE_PARAMETER_FIRST_NAME = "firstName";

	private static final String TEMPLATE_PARAMETER_CREDITED_OR_DEBITED = "creditedOrDebited";

	private static final String TEMPLATE_PARAMETER_QUANTITY = "quantity";

	private static final String TEMPLATE_PARAMETER_BALANCE = "balance";

	private static final Map<String, Object> TEMPLATE_VALUES = new HashMap<>();

	private final MailService mailService;

	@Autowired
	public BalanceUpdatedEmailNotification(MailService mailService)
	{
		this.mailService = mailService;
	}

	@EventHandler
	public void on(BalanceUpdatedEvent balanceUpdatedEvent)
	{
		String creditOrDebit = balanceUpdatedEvent.getQuantity() < 0 ? "debited" : "credited";

		populateTemplateValues(balanceUpdatedEvent.getFirstName(),
		                       creditOrDebit,
		                       Math.abs(balanceUpdatedEvent.getQuantity()),
		                       balanceUpdatedEvent.getNewBalance());

		mailService.sendEMail(balanceUpdatedEvent.getUsername(),
		                      "Your account has been " + creditOrDebit,
		                      BALANCE_UPDATE_NOTIFICATION_TEMPLATE,
		                      TEMPLATE_VALUES);
	}

	private void populateTemplateValues(String firstName, String creditOrDebit, Long quantity, BigDecimal newBalance)
	{
		TEMPLATE_VALUES.put(TEMPLATE_PARAMETER_FIRST_NAME, firstName);
		TEMPLATE_VALUES.put(TEMPLATE_PARAMETER_CREDITED_OR_DEBITED, creditOrDebit);
		TEMPLATE_VALUES.put(TEMPLATE_PARAMETER_QUANTITY, quantity);
		TEMPLATE_VALUES.put(TEMPLATE_PARAMETER_BALANCE, newBalance);
	}

}
