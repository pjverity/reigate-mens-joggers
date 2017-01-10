package uk.co.vhome.rmj.services;

import java.util.UUID;

/**
 * Interface for sending mails of predefined content
 */
public interface MailService extends ServiceAvailabilityReporter
{
	void sendRegistrationMail(String to, UUID token, String generatedPassword);
}
