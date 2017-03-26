package uk.co.vhome.rmj.site.admin;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.co.vhome.rmj.services.TokenManagementService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TokenManagementViewController
{
	private final TokenManagementService tokenManagementService;

	public TokenManagementViewController(TokenManagementService tokenManagementService)
	{
		this.tokenManagementService = tokenManagementService;
	}

	@PostMapping(value = "/admin/token-management")
	String post(@Valid TokenManagementFormObject formObject, BindingResult bindingResult)
	{
		if ( bindingResult.hasErrors() )
		{
			return "/admin/token-management";
		}

		formObject.getRows().stream()
				.filter(r -> r.getQuantity() != null)
				.forEach(r -> adjustBalance(r.getMemberBalance().getUsername(), r.getQuantity()));

		return "redirect:/admin/token-management";
	}

	private void adjustBalance(String username, int quantity)
	{
		tokenManagementService.creditAccount(username, quantity);
	}

	@GetMapping(value = "/admin/token-management")
	void get()
	{

	}

	@ModelAttribute
	public TokenManagementFormObject TokenManagementFormObject()
	{
		TokenManagementFormObject formObject = new TokenManagementFormObject();

		List<TokenManagementFormRow> rows = tokenManagementService.balanceForAllEnabledMembers()
				                                      .stream()
				                                      .map(TokenManagementFormRow::new)
				                                      .collect(Collectors.toList());
		formObject.setRows(rows);

		return formObject;
	}

}
