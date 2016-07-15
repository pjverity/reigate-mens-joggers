package uk.co.vhome.rmj.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class Home
{
	@RequestMapping(method = RequestMethod.GET)
	String getIndex()
	{
		return "index";
	}
}
