package uk.co.vhome.rmj.services;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import uk.co.vhome.rmj.entities.UserDetailsEntity;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutorService;

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

	private final ExecutorService executorService;

	@Inject
	public DefaultMailService(JavaMailSender javaMailSender, Configuration freemarkerConfiguration, ExecutorService executorService)
	{
		this.javaMailSender = javaMailSender;
		this.freemarkerConfiguration = freemarkerConfiguration;
		this.executorService = executorService;
	}

	/*
	 * Do not call as part of a transaction. If the mail fails to send we don't want the successful
	 * registration to roll back. Also, sending mail notifications is slow, so don't hold up the UI
	 * by hogging the request thread waiting for this to finish, execute mail sending tasks in another
	 * thread.
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void sendRegistrationMail(UserDetailsEntity newUserDetails, Set<UserDetailsEntity> enabledAdminDetails)
	{
		executorService.submit(() ->
		                       {
			                       Map<String, Object> templateProperties = new HashMap<>();

			                       templateProperties.put("firstName", newUserDetails.getFirstName());

			                       sendMailUsingTemplate(Collections.singletonList(newUserDetails),
			                                             "Welcome to Reigate Mens Joggers!",
			                                             templateProperties,
			                                             EMAIL_REGISTRATION_TEMPLATE);

			                       sendAdministratorMail(newUserDetails, enabledAdminDetails);
		                       });
	}

	private void sendAdministratorMail(UserDetailsEntity newUserDetails, Set<UserDetailsEntity> enabledAdminDetails)
	{
		Map<String, Object> templateProperties = new HashMap<>();

		templateProperties.put("user", newUserDetails);

		sendMailUsingTemplate(enabledAdminDetails,
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