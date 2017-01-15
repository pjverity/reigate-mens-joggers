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

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DefaultMailService implements MailService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String EMAIL_TEMPLATE = "registration-confirmation.txt";

	private static final String FROM_ADDRESS = "noreply@rmj.co.uk";

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
	public void sendRegistrationMail(String emailAddress, String firstName, UUID token, String generatedPassword)
	{
		if (!isServiceAvailable())
		{
			throw new IllegalStateException("Attempt to send mail when mail service unavailable");
		}

		try
		{
			String text = getMailContent(firstName, token);

			javaMailSender.send(mimeMessage ->
			{
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				message.setFrom(new InternetAddress(FROM_ADDRESS, FROM_NAME));
				message.setTo(emailAddress);
				message.setSubject("Confirm Registration to RJM");
				message.setText(text, true);
			});
		}
		catch (IOException | TemplateException e)
		{
			LOGGER.error("Failed to send registration confirmation mail", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isServiceAvailable()
	{
		return serviceAvailable;
	}

	private String getMailContent(String firstName, UUID token) throws IOException, TemplateException
	{
		Map<String, String> model = new HashMap<>();

		model.put("firstName", firstName);
		model.put("token", token.toString());

		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(EMAIL_TEMPLATE), model);
	}

}