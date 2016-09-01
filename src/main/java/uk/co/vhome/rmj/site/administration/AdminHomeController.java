package uk.co.vhome.rmj.site.administration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.co.vhome.rmj.site.AppUserDetails;

/**
 * Secured administration functions
 */
@Controller
@RequestMapping("admin")
@SuppressWarnings("unused")
public class AdminHomeController
{
	private static final Logger LOGGER = LogManager.getLogger();

	@RequestMapping(method = RequestMethod.GET)
	ModelAndView getAdminHome()
	{
		LOGGER.traceEntry();

		return LOGGER.traceExit(new ModelAndView("/jsp/administration/index", "userdetails", new AppUserDetails()));
	}

}
