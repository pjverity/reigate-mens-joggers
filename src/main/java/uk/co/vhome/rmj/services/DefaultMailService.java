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
import uk.co.vhome.rmj.entities.SupplementalUserDetails;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultMailService implements MailService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String EMAIL_REGISTRATION_TEMPLATE = "registration-confirmation.html";

	private static final String EMAIL_NOTIFICATION_TEMPLATE = "registration-notification.html";

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
	public void sendRegistrationMail(SupplementalUserDetails newUserDetails)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		templateProperties.put("firstName", newUserDetails.getFirstName());

		sendMailUsingTemplate(Collections.singletonList(newUserDetails),
		                      String.join(" ", newUserDetails.getFirstName(), newUserDetails.getLastName()),
		                      "Welcome to Reigate Mens Joggers!",
		                      templateProperties,
		                      EMAIL_REGISTRATION_TEMPLATE);
	}

	@Override
	public void sendAdministratorNotification(Collection<SupplementalUserDetails> administrators, SupplementalUserDetails newUserDetails)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		templateProperties.put("user", newUserDetails);

		sendMailUsingTemplate(administrators,
		                      "RMJ Admin",
		                      "New User Registered",
		                      templateProperties,
		                      EMAIL_NOTIFICATION_TEMPLATE);
	}

	private void sendMailUsingTemplate(Collection<SupplementalUserDetails> supplementalUserDetails, String name, String subject, Map<String, Object> templateProperties, String templateName)
	{
		try
		{
			String messageContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(templateName), templateProperties);
			sendMail(supplementalUserDetails, name, subject, messageContent);
		}
		catch (IOException | TemplateException e)
		{
			LOGGER.error("Failed to send registration confirmation mail", e);
			throw new RuntimeException(e);
		}

	}

	private void sendMail(Collection<SupplementalUserDetails> supplementalUserDetails, String name, String subject, String messageContent)
	{
		if (!isServiceAvailable())
		{
			throw new IllegalStateException("Attempt to send mail when mail service unavailable");
		}

		javaMailSender.send(mimeMessage ->
		{
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			message.setFrom(new InternetAddress(FROM_ADDRESS, FROM_NAME));
			supplementalUserDetails.forEach(details ->
			                                {
				                                try
				                                {
					                                message.addTo(details.getEmailAddress(), String.join(" ",details.getFirstName(), details.getLastName()));
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

	@Override
	public boolean isServiceAvailable()
	{
		return serviceAvailable;
	}

}