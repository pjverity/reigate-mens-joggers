package uk.co.vhome.rmj.services.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import uk.co.vhome.rmj.entities.Event;
import uk.co.vhome.rmj.entities.UserDetailsEntity;
import uk.co.vhome.rmj.security.AuthenticatedUser;
import uk.co.vhome.rmj.services.EventManagementService;
import uk.co.vhome.rmj.services.MailService;
import uk.co.vhome.rmj.services.TokenManagementService;
import uk.co.vhome.rmj.services.UserAccountManagementService;

import java.util.Optional;

@Service
public class DefaultHomeViewControllerService implements HomeViewControllerService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final MailService mailService;

	private final UserAccountManagementService userAccountManagementService;

	private final TokenManagementService tokenManagementService;

	private final EventManagementService eventManagementService;

	private final TransactionTemplate transactionTemplate;

	public DefaultHomeViewControllerService(MailService mailService, UserAccountManagementService userAccountManagementService, TokenManagementService tokenManagementService, EventManagementService eventManagementService, TransactionTemplate transactionTemplate)
	{
		this.mailService = mailService;
		this.userAccountManagementService = userAccountManagementService;
		this.tokenManagementService = tokenManagementService;
		this.eventManagementService = eventManagementService;
		this.transactionTemplate = transactionTemplate;
	}

	@Override
	public Optional<Event> findNextEvent()
	{
		return eventManagementService.findAllIncompleteEvents().stream().findFirst();
	}

	@Override
	public void registerNewUser(String username, String firstName, String lastName, String password)
	{
		AuthenticatedUser.runWithSystemUser(() ->
		                                    {
			                                    UserDetailsEntity newUserEntity = createNewUserAndCreditFreeToken(username, firstName, lastName, password);
			                                    sendNotificationMails(newUserEntity);
		                                    });
	}

	private UserDetailsEntity createNewUserAndCreditFreeToken(String username, String firstName, String lastName, String password)
	{
		// This is run in it's own transaction so that any 'non-essential' work (i.e., sending notification
		// mails) done subsequently does not cause this to roll back.
		return transactionTemplate.execute((transactionStatus) ->
		                                   {
			                                   UserDetailsEntity userEntity = userAccountManagementService.registerNewUser(username, firstName, lastName, password);
			                                   tokenManagementService.creditAccount(username, 1);
			                                   return userEntity;
		                                   });
	}

	private void sendNotificationMails(UserDetailsEntity newUserEntity)
	{
		if (newUserEntity != null)
		{
			try
			{
				mailService.sendRegistrationMail(newUserEntity);
				mailService.sendAdministratorNotification(newUserEntity);
			}
			catch (Exception e)
			{
				LOGGER.warn("Failed to send registration mails to user and/or administrators for newly registered user: {}", newUserEntity.getUsername());
			}
		}
	}

}
