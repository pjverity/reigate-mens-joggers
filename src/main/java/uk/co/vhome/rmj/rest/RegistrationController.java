package uk.co.vhome.rmj.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.vhome.rmj.services.UserRegistrationService;

import java.util.UUID;

/**
 * A REST interface for user registrations. Currently only useful for
 * problem resolution
 */
@RestController
public class RegistrationController
{
	private final UserRegistrationService userRegistrationService;

	public RegistrationController(UserRegistrationService userRegistrationService)
	{
		this.userRegistrationService = userRegistrationService;
	}

	@RequestMapping("/confirm/{uuid}")
	public String confirm(@PathVariable(name = "uuid") UUID uuid)
	{
		try
		{
			userRegistrationService.confirmRegistration(uuid);
			return "success";
		}
		catch (Exception e)
		{
			return "failed: " + e.getMessage();
		}
	}

	@RequestMapping("/rescind/{uuid}")
	public String rescind(@PathVariable(name = "uuid") UUID uuid)
	{
		try
		{
			userRegistrationService.rescindRegistration(uuid);
			return "success";
		}
		catch (Exception e)
		{
			return "failed: " + e.getMessage();
		}
	}

}
