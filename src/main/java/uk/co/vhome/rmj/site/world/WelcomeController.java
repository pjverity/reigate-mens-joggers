package uk.co.vhome.rmj.site.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.vhome.rmj.repositories.DefaultUserRepository;

import javax.inject.Inject;

@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class WelcomeController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final DefaultUserRepository userRepository;

	@Inject
	public WelcomeController(DefaultUserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	String getIndex()
	{
		LOGGER.traceEntry();

		userRepository.getAll();

		return LOGGER.traceExit("/jsp/world/index");
	}
}
