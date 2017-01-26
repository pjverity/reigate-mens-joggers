package uk.co.vhome.rmj.services;

/**
 * Interface for services pertaining to registering new users
 */
public interface UserRegistrationService extends ServiceAvailabilityReporter
{
	void registerNewUser(String userId, String firstName, String lastName, String password);

	void changePassword(String userId, String oldPassword, String newPassword);
}