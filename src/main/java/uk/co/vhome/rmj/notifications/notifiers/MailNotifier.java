package uk.co.vhome.rmj.notifications.notifiers;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.notifications.BalanceUpdatedNotification;
import uk.co.vhome.rmj.notifications.LowBalanceNotification;
import uk.co.vhome.rmj.notifications.NewUserNotification;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailNotifier implements Notifier
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String LOW_BALANCE_NOTIFICATION_TEMPLATE = "low-balance-notification.html";

	private static final String BALANCE_UPDATE_NOTIFICATION_TEMPLATE = "balance-update-notification.html";

	private static final String REGISTRATION_NOTIFICATION_TEMPLATE = "registration-notification.html";

	private static final String REGISTRATION_CONFIRMATION_TEMPLATE = "registration-confirmation.html";

	private static final MessageFormat TOKENS_FORMAT = new MessageFormat("{0,choice,-2<{0,number,integer} tokens|-1#-1 token|0#0 tokens|1#1 token|1<{0,number,integer} tokens}");

	private static final String FROM_ADDRESS = "noreply@reigatemensjoggers.co.uk";

	private static final String FROM_NAME = "Reigate Mens Joggers";

	private final JavaMailSender javaMailSender;

	private final Configuration freemarkerConfiguration;

	@Inject
	public MailNotifier(JavaMailSender javaMailSender, Configuration freemarkerConfiguration)
	{
		this.javaMailSender = javaMailSender;
		this.freemarkerConfiguration = freemarkerConfiguration;
	}

	@Override
	public void on(LowBalanceNotification notification)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		String quantity = TOKENS_FORMAT.format(new Object[]{Math.abs(notification.getQuantity())});
		String balance = TOKENS_FORMAT.format(new Object[]{notification.getCurrentBalance()});

		templateProperties.put("firstName", notification.getUserDetails().getFirstName());
		templateProperties.put("lastName", notification.getUserDetails().getLastName());

		templateProperties.put("quantity", quantity);
		templateProperties.put("balance", balance);

		sendMailUsingTemplate(Collections.singletonList(notification.getUserDetails()),
		                      "Low balance alert",
		                      templateProperties,
		                      LOW_BALANCE_NOTIFICATION_TEMPLATE);

	}

	@Override
	public void on(BalanceUpdatedNotification notification)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		String quantity = TOKENS_FORMAT.format(new Object[]{Math.abs(notification.getQuantity())});
		String balance = TOKENS_FORMAT.format(new Object[]{notification.getBalance()});

		String creditedOrDebited = notification.getQuantity() < 0 ? "debited" : "credited";

		templateProperties.put("firstName", notification.getUserDetails().getFirstName());
		templateProperties.put("lastName", notification.getUserDetails().getLastName());

		templateProperties.put("quantity", quantity);
		templateProperties.put("balance", balance);
		templateProperties.put("creditedOrDebited", creditedOrDebited);

		sendMailUsingTemplate(Collections.singletonList(notification.getUserDetails()),
		                      "Your account has been " + creditedOrDebited,
		                      templateProperties,
		                      BALANCE_UPDATE_NOTIFICATION_TEMPLATE);

	}

	@Override
	public void on(NewUserNotification notification)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		templateProperties.put("firstName", notification.getUserDetails().getFirstName());

		sendMailUsingTemplate(Collections.singletonList(notification.getUserDetails()),
		                      "Welcome to Reigate Mens Joggers!",
		                      templateProperties,
		                      REGISTRATION_CONFIRMATION_TEMPLATE);

		templateProperties = new HashMap<>();

		templateProperties.put("user", notification.getUserDetails());

		sendMailUsingTemplate(notification.getEnabledAdminDetails(),
		                      "New User Registered",
		                      templateProperties,
		                      REGISTRATION_NOTIFICATION_TEMPLATE);

	}

	private void sendMailUsingTemplate(Collection<UserDetailsEntity> recipientUserDetails, String subject, Map<String, Object> templateProperties, String templateName)
	{
		try
		{
			String messageContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(templateName), templateProperties);
			sendMail(recipientUserDetails, subject, messageContent);
		}
		catch (IOException | TemplateException e)
		{
			LOGGER.error("Failed to send registration confirmation mail", e);
			throw new RuntimeException(e);
		}

	}

	private void sendMail(Collection<UserDetailsEntity> userDetailEntities, String subject, String messageContent)
	{
		javaMailSender.send(mimeMessage ->
		                    {
			                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			                    message.setFrom(new InternetAddress(FROM_ADDRESS, FROM_NAME));
			                    userDetailEntities.forEach(details ->
			                                               {
				                                               try
				                                               {
					                                               message.addTo(details.getUsername(), String.join(" ", details.getFirstName(), details.getLastName()));
				                                               }
				                                               catch (MessagingException | UnsupportedEncodingException e)
				                                               {
					                                               LOGGER.error("Failed to add recipient", e);
				                                               }
			                                               });
			                    message.setSubject(subject);
			                    message.setText(messageContent, true);
		                    });
	}
}