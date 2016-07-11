package uk.co.vhome.rmj.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class Home
{
	@RequestMapping(method = RequestMethod.GET)
	protected View getHome()
	{
		return new RedirectView("home", true);
	}

	@RequestMapping(value = "home", method = RequestMethod.GET)
	String getIndex()
	{
		return "home/index";
	}
}
