package uk.co.vhome.clubbed.mail.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
class DefaultEmailService implements MailService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEmailService.class);

	private final String fromAddress;

	private final String fromName;

	private final JavaMailSender mailSender;

	private final Configuration freemarkerConfiguration;

	public DefaultEmailService(JavaMailSender mailSender,
	                           @Value("${mail.from.address}") String fromAddress,
	                           @Value("${mail.from.name}") String fromName,
	                           Configuration freemarkerConfiguration)
	{
		this.mailSender = mailSender;
		this.fromAddress = fromAddress;
		this.fromName = fromName;
		this.freemarkerConfiguration = freemarkerConfiguration;
	}

	private String generateMessage(String templateName, Map<String, Object> templateParameters) throws IOException, TemplateException
	{
		Template template = freemarkerConfiguration.getTemplate(templateName);
		return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateParameters);
	}

	private MimeMessageHelper getBaseMessage(String toAddress, String subject) throws UnsupportedEncodingException, MessagingException
	{
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailSender.createMimeMessage());
		mimeMessageHelper.addTo(toAddress);
		mimeMessageHelper.setFrom(fromAddress, fromName);
		mimeMessageHelper.setSubject(subject);
		return mimeMessageHelper;
	}

	@Override
	public void sendEMail(String toAddress, String subject, String templateName, Map<String, Object> templateValues)
	{
		doSend(toAddress, subject, templateName, templateValues, false);
	}

	@Override
	public void sendEMailAndBccAdmin(String toAddress, String subject, String templateName, Map<String, Object> templateValues)
	{
		doSend(toAddress, subject, templateName, templateValues, true);
	}

	private void doSend(String toAddress, String subject, String templateName, Map<String, Object> templateValues, boolean bccAdmin)
	{
		try
		{
			MimeMessageHelper mimeMessageHelper = getBaseMessage(toAddress, subject);
			String htmlMessageContent = generateMessage(templateName, templateValues);
			mimeMessageHelper.setText(htmlMessageContent, true);

			if (bccAdmin)
			{
				mimeMessageHelper.addBcc(fromAddress, fromName);
			}

			mailSender.send(mimeMessageHelper.getMimeMessage());
		}
		catch (MessagingException | IOException | TemplateException e)
		{
			LOGGER.error("Failed to send e-mail notification", e);
		}

	}
}
