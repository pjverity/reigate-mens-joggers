package uk.co.vhome.rmj.site.member;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.vhome.rmj.services.TokenManagementService;

import java.security.Principal;

/**
 * Member home screen
 */
@Controller
public class HomeViewController
{
	private final TokenManagementService tokenManagementService;

	public HomeViewController(TokenManagementService tokenManagementService)
	{
		this.tokenManagementService = tokenManagementService;
	}

	@RequestMapping(value = "/member/home", method = RequestMethod.GET)
	public void home(@AuthenticationPrincipal Principal principal, ModelMap modelMap)
	{
		modelMap.put("tokenCount", tokenManagementService.balanceForMember(principal.getName()));
	}
}
