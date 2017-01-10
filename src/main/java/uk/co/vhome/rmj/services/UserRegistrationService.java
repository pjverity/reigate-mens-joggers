package uk.co.vhome.rmj.services;

import java.util.Set;

/**
 * Interface for services pertaining to registering new users
 */
public interface UserRegistrationService extends ServiceAvailabilityReporter
{
	Set<String> generateRegistration(String firstName, String lastName, String emailAddress);

	boolean isEmailAddressInUse(String emailAddress);
}