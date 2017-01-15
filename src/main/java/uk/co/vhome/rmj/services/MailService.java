package uk.co.vhome.rmj.services;

import java.util.UUID;

/**
 * Interface for sending mails of predefined content
 */
public interface MailService extends ServiceAvailabilityReporter
{
	void sendRegistrationMail(String emailAddress, String firstName, UUID token, String generatedPassword);
}
