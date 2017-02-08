package uk.co.vhome.rmj.services;

import uk.co.vhome.rmj.entities.SupplementalUserDetails;

import java.util.Collection;

/**
 * Interface for sending mails of predefined content
 */
public interface MailService extends ServiceAvailabilityReporter
{
	void sendRegistrationMail(SupplementalUserDetails newUserDetails);

	void sendAdministratorNotification(Collection<SupplementalUserDetails> administrators, SupplementalUserDetails newUserDetails);
}
