package uk.co.vhome.rmj.notifications.notifiers;

import uk.co.vhome.rmj.entities.UserDetailsEntity;

import java.util.Collection;
import java.util.Map;

public interface Notifier
{
	void sendMailUsingTemplate(Collection<UserDetailsEntity> recipientUserDetails, String subject, Map<String, Object> templateProperties, String templateName);
}
