package uk.co.vhome.rmj.services;

import java.util.UUID;

/**
 * Interface for services pertaining to registering new users
 */
public interface UserRegistrationService extends ServiceAvailabilityReporter
{
	void generateRegistration(String firstName, String lastName, String emailAddress);

	void confirmRegistration(UUID uuid);

	void rescindRegistration(UUID uuid);

	boolean isEmailAddressInUse(String emailAddress);
}