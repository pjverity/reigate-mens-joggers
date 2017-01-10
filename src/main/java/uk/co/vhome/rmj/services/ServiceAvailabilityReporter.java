package uk.co.vhome.rmj.services;

/**
 * Implemented by services to report their availability so that system functions can
 * fail gracefully
 */
public interface ServiceAvailabilityReporter
{
	boolean isServiceAvailable();
}
