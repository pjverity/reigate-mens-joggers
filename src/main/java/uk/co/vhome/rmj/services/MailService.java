package uk.co.vhome.rmj.services;

/**
 * Interface for sending mails of predefined content
 */
public interface MailService extends ServiceAvailabilityReporter
{
	void sendRegistrationMail(String emailAddress, String firstName);
}
