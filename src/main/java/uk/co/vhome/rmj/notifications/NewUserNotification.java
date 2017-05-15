package uk.co.vhome.rmj.notifications;

import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.notifications.notifiers.MailNotifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NewUserNotification extends NotificationTask
{
	private static final String EMAIL_NOTIFICATION_TEMPLATE = "registration-notification.html";

	private static final String EMAIL_REGISTRATION_TEMPLATE = "registration-confirmation.html";

	private final UserDetailsEntity newUserDetails;

	private final Set<UserDetailsEntity> enabledAdminDetails;

	public NewUserNotification(UserDetailsEntity newUserDetails, Set<UserDetailsEntity> enabledAdminDetails)
	{
		this.newUserDetails = newUserDetails;
		this.enabledAdminDetails = enabledAdminDetails;
	}

	@Override
	public void performNotification(MailNotifier mailNotifier)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		templateProperties.put("firstName", newUserDetails.getFirstName());

		mailNotifier.sendMailUsingTemplate(Collections.singletonList(newUserDetails),
		                                  "Welcome to Reigate Mens Joggers!",
		                                  templateProperties,
		                                  EMAIL_REGISTRATION_TEMPLATE);

		templateProperties = new HashMap<>();

		templateProperties.put("user", newUserDetails);

		mailNotifier.sendMailUsingTemplate(enabledAdminDetails,
		                                  "New User Registered",
		                                  templateProperties,
		                                  EMAIL_NOTIFICATION_TEMPLATE);

	}
}
