package uk.co.vhome.rmj.notifications.notifiers;

import uk.co.vhome.rmj.notifications.BalanceUpdatedNotification;
import uk.co.vhome.rmj.notifications.LowBalanceNotification;
import uk.co.vhome.rmj.notifications.NewUserNotification;

public interface Notifier
{
	void on(LowBalanceNotification notification);

	void on(BalanceUpdatedNotification notification);

	void on(NewUserNotification notification);
}
