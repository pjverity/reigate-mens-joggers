package uk.co.vhome.rmj.services;

import uk.co.vhome.rmj.entities.User;

/**
 * Interface for sending mails of predefined content
 */
public interface MailService extends ServiceAvailabilityReporter
{
	void sendRegistrationMail(User user);

	void sendAdministratorNotification(User user);
}
