package uk.co.vhome.rmj.services;

import uk.co.vhome.rmj.entities.UserDetail;

/**
 * Interface for sending mails of predefined content
 */
public interface MailService extends ServiceAvailabilityReporter
{
	void sendRegistrationMail(UserDetail userDetail);

	void sendAdministratorNotification(UserDetail userDetail);
}
