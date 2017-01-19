package uk.co.vhome.rmj.services;

import java.util.UUID;

/**
 * Interface for services pertaining to registering new users
 */
public interface UserRegistrationService extends ServiceAvailabilityReporter
{
	void generateRegistration(String userId, String firstName, String lastName);

	void confirmRegistration(UUID uuid);

	void rescindRegistration(UUID uuid);

	void changePassword(String userId, String oldPassword, String newPassword);
}