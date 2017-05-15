package uk.co.vhome.rmj.notifications;

import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.notifications.notifiers.MailNotifier;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BalanceUpdatedNotification extends NotificationTask
{
	private static final String EMAIL_BALANCE_UPDATE_NOTIFICATION_TEMPLATE = "balance-update-notification.html";

	private static final MessageFormat TOKENS_FORMAT = new MessageFormat("{0,choice,-2<{0,number,integer} tokens|-1#-1 token|0#0 tokens|1#1 token|1<{0,number,integer} tokens}");

	private final UserDetailsEntity userDetails;

	private final int quantity;

	private final Integer balance;


	public BalanceUpdatedNotification(UserDetailsEntity userDetails, int quantity, Integer balance)
	{
		this.userDetails = userDetails;
		this.quantity = quantity;
		this.balance = balance;
	}

	@Override
	void performNotification(MailNotifier mailNotifier)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		String quantity = TOKENS_FORMAT.format(new Object[]{Math.abs(this.quantity)});
		String balance = TOKENS_FORMAT.format(new Object[]{this.balance});

		String creditedOrDebited = this.quantity < 0 ? "debited by" : "credited with";

		templateProperties.put("firstName", userDetails.getFirstName());
		templateProperties.put("quantity", quantity);
		templateProperties.put("balance", balance);
		templateProperties.put("creditedOrDebited", creditedOrDebited);

		mailNotifier.sendMailUsingTemplate(Collections.singletonList(userDetails),
		                                   "Your balance has been updated",
		                                   templateProperties,
		                                   EMAIL_BALANCE_UPDATE_NOTIFICATION_TEMPLATE);

	}
}
