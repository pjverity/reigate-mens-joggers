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

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

@Component
public class MailNotifier implements Notifier
{
	private static final Logger LOGGER = LogManager.getLogger();

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

	public void sendMailUsingTemplate(Collection<UserDetailsEntity> recipientUserDetails, String subject, Map<String, Object> templateProperties, String templateName)
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