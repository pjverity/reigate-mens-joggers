package uk.co.vhome.rmj.services;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import uk.co.vhome.rmj.entities.UserDetailsEntity;

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

	@Inject
	public DefaultMailService(JavaMailSender javaMailSender, Configuration freemarkerConfiguration)
	{
		this.javaMailSender = javaMailSender;
		this.freemarkerConfiguration = freemarkerConfiguration;
	}

	@Override
	public void sendRegistrationMail(UserDetailsEntity newUserDetails)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		templateProperties.put("firstName", newUserDetails.getFirstName());

		sendMailUsingTemplate(Collections.singletonList(newUserDetails),
		                      "Welcome to Reigate Mens Joggers!",
		                      templateProperties,
		                      EMAIL_REGISTRATION_TEMPLATE);
	}

	@Override
	public void sendAdministratorNotification(Collection<UserDetailsEntity> administrators, UserDetailsEntity newUserDetails)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		templateProperties.put("user", newUserDetails);

		sendMailUsingTemplate(administrators,
		                      "New User Registered",
		                      templateProperties,
		                      EMAIL_NOTIFICATION_TEMPLATE);
	}

	private void sendMailUsingTemplate(Collection<UserDetailsEntity> userDetailEntities, String subject, Map<String, Object> templateProperties, String templateName)
	{
		try
		{
			String messageContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(templateName), templateProperties);
			sendMail(userDetailEntities, subject, messageContent);
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