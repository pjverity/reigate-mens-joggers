package uk.co.vhome.rmj.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.MessagingException;
import java.util.UUID;

@Service
public class DefaultMailService implements MailService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private boolean serviceAvailable = false;

	private final MailSender mailSender;

	@Inject
	public DefaultMailService(MailSender mailSender)
	{
		this.mailSender = mailSender;

		try
		{
			if ( mailSender instanceof JavaMailSenderImpl )
			{
				((JavaMailSenderImpl) mailSender).testConnection();
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
	public void sendRegistrationMail(String to, UUID token, String generatedPassword)
	{
		if ( !isServiceAvailable() )
		{
			throw new IllegalStateException("Attempt to send mail when mail service unavilable");
		}

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(to);
		message.setFrom("noreply@rmj.co.uk");
		message.setSubject("Sign up!");
		message.setText("Register: " + token + "/" + generatedPassword);

		mailSender.send(message);
	}

	@Override
	public boolean isServiceAvailable()
	{
		return serviceAvailable;
	}

}