package uk.co.vhome.rmj.services;

import uk.co.vhome.rmj.entities.SupplementalUserDetails;

/**
 * Interface for sending mails of predefined content
 */
public interface MailService extends ServiceAvailabilityReporter
{
	void sendRegistrationMail(SupplementalUserDetails supplementalUserDetails);

	void sendAdministratorNotification(SupplementalUserDetails supplementalUserDetails);
}
