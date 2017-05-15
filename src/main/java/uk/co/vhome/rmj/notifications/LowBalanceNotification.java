package uk.co.vhome.rmj.notifications;

import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.notifications.notifiers.MailNotifier;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LowBalanceNotification extends NotificationTask
{
	private static final String EMAIL_NOTIFICATION_TEMPLATE = "low-balance-notification.html";

	private static final MessageFormat TOKENS_FORMAT = new MessageFormat("{0,choice,-2<{0,number,integer} tokens|-1#-1 token|0#0 tokens|1#1 token|1<{0,number,integer} tokens}");

	private final UserDetailsEntity userDetails;

	private final int quantity;

	private final int currentBalance;

	public LowBalanceNotification(UserDetailsEntity userDetails, int quantity, int currentBalance)
	{
		this.userDetails = userDetails;
		this.quantity = quantity;
		this.currentBalance = currentBalance;
	}

	@Override
	public void performNotification(MailNotifier mailNotifier)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		String quantity = TOKENS_FORMAT.format(new Object[]{Math.abs(this.quantity)});
		String balance = TOKENS_FORMAT.format(new Object[]{currentBalance});

		templateProperties.put("firstName", userDetails.getFirstName());
		templateProperties.put("quantity", quantity);
		templateProperties.put("balance", balance);

		mailNotifier.sendMailUsingTemplate(Collections.singletonList(userDetails),
		                                  "Low Balance Alert!",
		                                  templateProperties,
		                                  EMAIL_NOTIFICATION_TEMPLATE);
	}
}
