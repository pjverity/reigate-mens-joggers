package uk.co.vhome.rmj.site;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class Home
{
	private static final Logger LOGGER = LogManager.getLogger();

	@RequestMapping(method = RequestMethod.GET)
	String getIndex()
	{
		LOGGER.traceEntry();

		return LOGGER.traceExit("index");
	}
}
