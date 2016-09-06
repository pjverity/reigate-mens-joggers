package uk.co.vhome.rmj.site.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.co.vhome.rmj.site.AppUserDetails;

@Controller
@RequestMapping("account")
public class AccountController
{
	private static final Logger LOGGER = LogManager.getLogger();

	@RequestMapping(method = RequestMethod.GET)
	ModelAndView getAccountDetails()
	{
		LOGGER.traceEntry();

		return LOGGER.traceExit(new ModelAndView("/jsp/user/account", "userdetails", new AppUserDetails()));
	}

}
