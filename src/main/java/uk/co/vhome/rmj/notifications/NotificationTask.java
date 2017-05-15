package uk.co.vhome.rmj.notifications;

import uk.co.vhome.rmj.notifications.notifiers.MailNotifier;
import uk.co.vhome.rmj.security.AuthenticatedUser;

/**
 * Extended be classes to implement the notification logic. This occurs asynchronously so
 * implementation should be thread safe. The implementation is called as the system user.
 */
public abstract class NotificationTask implements Runnable
{
	private MailNotifier mailNotifier;

	abstract void performNotification(MailNotifier mailNotifier);

	public void setMailNotifier(MailNotifier mailNotifier)
	{
		this.mailNotifier = mailNotifier;
	}

	@Override
	public void run()
	{
		AuthenticatedUser.runWithSystemUser(() -> performNotification(mailNotifier));
	}

}
