package uk.co.vhome.rmj.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.notifications.NotificationTask;
import uk.co.vhome.rmj.notifications.notifiers.MailNotifier;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

/**
 * Simple notification service that currently uses a single-threaded executor to
 * execute implementations of {@link NotificationTask}'s. The only notification mechanism
 * is currently a mail based service which is supplied to the task before execution.
 */
@Service
public class AsyncNotificationService implements NotificationService
{
	private final MailNotifier mailNotifier;

	private final ExecutorService executorService;

	@Inject
	public AsyncNotificationService(MailNotifier mailNotifier, ExecutorService executorService)
	{
		this.mailNotifier = mailNotifier;
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
	public void postNotification(NotificationTask notificationTask)
	{
		notificationTask.setMailNotifier(mailNotifier);
		executorService.submit(notificationTask);
	}

}
