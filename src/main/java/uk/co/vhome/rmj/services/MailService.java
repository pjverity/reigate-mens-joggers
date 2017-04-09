package uk.co.vhome.rmj.services;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.security.Role;

/**
 * Interface for sending mails of predefined content
 */
public interface MailService
{
	@Secured({Role.SYSTEM, Role.RUN_AS_SYSTEM})
	void sendRegistrationMail(UserDetailsEntity newUserDetails);

	@Secured({Role.SYSTEM, Role.RUN_AS_SYSTEM})
	void sendAdministratorNotification(UserDetailsEntity newUserDetails);
}
