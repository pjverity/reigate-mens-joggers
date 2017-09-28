package uk.co.vhome.rmj.config;

import org.springframework.stereotype.Component;
import uk.co.vhome.clubbed.notifications.BalanceUpdatedNotification;
import uk.co.vhome.clubbed.notifications.LowBalanceNotification;
import uk.co.vhome.clubbed.notifications.NewUserNotification;
import uk.co.vhome.clubbed.notifications.metadatasuppliers.MailMetaData;
import uk.co.vhome.clubbed.notifications.metadatasuppliers.NotificationMetaDataSupplier;

@Component
public class MailNotificationMetaDataSupplier implements NotificationMetaDataSupplier<MailMetaData>
{
	private static final String FROM_ADDRESS = "admin@reigatemensjoggers.co.uk";

	private static final String FROM_NAME = "Reigate Mens Joggers";

	private static final String LOW_BALANCE_NOTIFICATION_TEMPLATE = "low-balance-notification.html";

	private static final String BALANCE_UPDATE_NOTIFICATION_TEMPLATE = "balance-update-notification.html";

	private static final String REGISTRATION_CONFIRMATION_TEMPLATE = "registration-confirmation.html";

	private static final MailMetaData LOW_BALANCE_ALERT_MAIL_META_DATA = new MailMetaData(FROM_ADDRESS,
	                                                                                      FROM_NAME,
	                                                                                      LOW_BALANCE_NOTIFICATION_TEMPLATE,
	                                                                                      "Low balance alert");

	private static final MailMetaData NEW_USER_MAIL_META_DATA = new MailMetaData(FROM_ADDRESS,
	                                                                             FROM_NAME,
	                                                                             REGISTRATION_CONFIRMATION_TEMPLATE,
	                                                                             "Welcome to Reigate Mens Joggers!");

	private static final MailMetaData BALANCE_CREDITED_MAIL_META_DATA = new MailMetaData(FROM_ADDRESS,
	                                                                                     FROM_NAME,
	                                                                                     BALANCE_UPDATE_NOTIFICATION_TEMPLATE,
	                                                                                     "Your account has been credited");

	private static final MailMetaData BALANCE_DEBITED_MAIL_META_DATA = new MailMetaData(FROM_ADDRESS,
	                                                                                    FROM_NAME,
	                                                                                    BALANCE_UPDATE_NOTIFICATION_TEMPLATE,
	                                                                                    "Your account has been debited");

	@Override
	public MailMetaData metaDataFor(LowBalanceNotification notification)
	{
		return LOW_BALANCE_ALERT_MAIL_META_DATA;
	}

	@Override
	public MailMetaData metaDataFor(BalanceUpdatedNotification notification)
	{
		return notification.getQuantity() < 0 ? BALANCE_DEBITED_MAIL_META_DATA : BALANCE_CREDITED_MAIL_META_DATA;
	}

	@Override
	public MailMetaData metaDataFor(NewUserNotification notification)
	{
		return NEW_USER_MAIL_META_DATA;
	}

}
