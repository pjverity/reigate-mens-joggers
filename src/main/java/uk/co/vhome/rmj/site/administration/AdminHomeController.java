package uk.co.vhome.rmj.site.administration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.vhome.rmj.services.UserService;

import javax.inject.Inject;

/**
 * Secured administration functions
 */
@Controller
@RequestMapping("admin")
@SuppressWarnings("unused")
public class AdminHomeController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final UserService userService;

	@Inject
	public AdminHomeController(UserService userService)
	{
		this.userService = userService;
	}


	@RequestMapping(method = RequestMethod.GET)
	String getAdminHome()
	{
		LOGGER.traceEntry();

		return LOGGER.traceExit("/jsp/administration/index");
	}

}
