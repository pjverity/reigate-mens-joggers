package uk.co.vhome.rmj.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.notifications.BalanceUpdatedNotification;
import uk.co.vhome.rmj.notifications.LowBalanceNotification;
import uk.co.vhome.rmj.notifications.NewUserNotification;
import uk.co.vhome.rmj.notifications.notifiers.Notifier;
import uk.co.vhome.rmj.security.AuthenticatedUser;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

/**
 * Simple notification service that currently uses a single-threaded executor to
 * execute notification events. The only notification mechanism
 * is currently a mail based service which is supplied to the task before execution.
 */
@Service
public class AsyncNotificationService implements NotificationService
{
	private final Notifier notifier;

	private final ExecutorService executorService;

	@Inject
	public AsyncNotificationService(Notifier notifier, ExecutorService executorService)
	{
		this.notifier = notifier;
		this.executorService = executorService;
	}

	/*
	 * Do not call as part of a transaction. If the mail fails to send we don't want the successful
	 * registration to roll back. Also, sending mail notifications is slow, so don't hold up the UI
	 * by hogging the request thread waiting for this to finish, execute mail sending tasks in another
	 * thread.
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void postNotification(Object notification)
	{
		executorService.submit(() ->
				                       AuthenticatedUser.runWithSystemUser(() ->
				                                                           {
					                                                           if (notification instanceof NewUserNotification)
					                                                           {
						                                                           notifier.on(((NewUserNotification) notification));
					                                                           }
					                                                           else if (notification instanceof LowBalanceNotification)
					                                                           {
						                                                           notifier.on(((LowBalanceNotification) notification));
					                                                           }
					                                                           else if (notification instanceof BalanceUpdatedNotification)
					                                                           {
						                                                           notifier.on(((BalanceUpdatedNotification) notification));
					                                                           }
				                                                           }));
	}

}
