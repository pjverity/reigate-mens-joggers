package uk.co.vhome.rmj.services;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.security.Role;

import java.util.Set;

/**
 * Interface for sending mails of predefined content
 */
public interface MailService
{
	@Secured({Role.SYSTEM})
	void sendRegistrationMail(UserDetailsEntity newUserDetails, Set<UserDetailsEntity> enabledAdminDetails);
}