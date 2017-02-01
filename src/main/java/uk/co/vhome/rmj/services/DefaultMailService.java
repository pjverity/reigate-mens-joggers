package uk.co.vhome.rmj.services;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import uk.co.vhome.rmj.entities.User;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultMailService implements MailService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String EMAIL_REGISTRATION_TEMPLATE = "registration-confirmation.html";

	private static final String EMAIL_NOTIFICATION_TEMPLATE = "registration-notification.html";

	private static final String ADMIN_ADDRESS = "administrator@reigatemensjoggers.co.uk";

	private static final String FROM_ADDRESS = "noreply@reigatemensjoggers.co.uk";

	private static final String FROM_NAME = "Reigate Mens Joggers";

	private final JavaMailSender javaMailSender;

	private final Configuration freemarkerConfiguration;

	private boolean serviceAvailable = false;

	@Inject
	public DefaultMailService(JavaMailSender javaMailSender, Configuration freemarkerConfiguration)
	{
		this.javaMailSender = javaMailSender;
		this.freemarkerConfiguration = freemarkerConfiguration;

		try
		{
			if (javaMailSender instanceof JavaMailSenderImpl)
			{
				((JavaMailSenderImpl) javaMailSender).testConnection();
				serviceAvailable = true;
			}
		}
		catch (MessagingException e)
		{
			serviceAvailable = false;
			LOGGER.error("Connection to mail server failed. Systems sending e-mail notification will be disabled", e);
		}
	}

	@Override
	public void sendRegistrationMail(User user)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		templateProperties.put("firstName", user.getUserDetail().getFirstName());

		sendMailUsingTemplate(user.getUsername(),
				String.join(" ", user.getUserDetail().getFirstName(), user.getUserDetail().getLastName()),
				"Welcome to Reigate Mens Joggers!",
				templateProperties,
				EMAIL_REGISTRATION_TEMPLATE);
	}

	@Override
	public void sendAdministratorNotification(User user)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		templateProperties.put("user", user);

		sendMailUsingTemplate(ADMIN_ADDRESS,
				"RMJ Admin",
				"New User Registered",
				templateProperties,
				EMAIL_NOTIFICATION_TEMPLATE);
	}

	private void sendMailUsingTemplate(String emailAddress, String name, String subject, Map<String, Object> templateProperties, String templateName)
	{
		try
		{
			String messageContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(templateName), templateProperties);
			sendMail(emailAddress, name, subject, messageContent);
		}
		catch (IOException | TemplateException e)
		{
			LOGGER.error("Failed to send registration confirmation mail", e);
			throw new RuntimeException(e);
		}

	}

	private void sendMail(String emailAddress, String name, String subject, String messageContent)
	{
		if (!isServiceAvailable())
		{
			throw new IllegalStateException("Attempt to send mail when mail service unavailable");
		}

		javaMailSender.send(mimeMessage ->
		{
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			message.setFrom(new InternetAddress(FROM_ADDRESS, FROM_NAME));
			message.setTo(new InternetAddress(emailAddress, name));
			message.setSubject(subject);
			message.setText(messageContent, true);
		});
	}

	@Override
	public boolean isServiceAvailable()
	{
		return serviceAvailable;
	}

}