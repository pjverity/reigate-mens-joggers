package uk.co.vhome.rmj.services;

import uk.co.vhome.rmj.notifications.NotificationTask;

public interface NotificationService
{
	void postNotification(NotificationTask notificationTask);
}
