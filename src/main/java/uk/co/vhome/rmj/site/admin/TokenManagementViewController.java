package uk.co.vhome.rmj.site.admin;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.services.core.TokenManagementService;

import javax.validation.Valid;
import java.util.Comparator;
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
		if (bindingResult.hasErrors())
		{
			return "/admin/token-management";
		}

		formObject.getRows().stream()
				.filter(r -> r.getQuantity() != null)
				.forEach(r -> tokenManagementService.creditAccount(r.getMemberBalance().getUserId(), r.getQuantity()));

		return "redirect:/admin/token-management";
	}

	@GetMapping(value = "/admin/token-management")
	void get()
	{
	}

	@ModelAttribute
	public TokenManagementFormObject tokenManagementFormObject()
	{
		TokenManagementFormObject formObject = new TokenManagementFormObject();

		List<TokenManagementFormRow> rows = tokenManagementService.balancesForAllEnabledMembers()
				                                    .stream()
				                                    .sorted(Comparator.comparing(MemberBalance::getLastName).thenComparing(Comparator.comparing(MemberBalance::getFirstName)))
				                                    .map(TokenManagementFormRow::new)
				                                    .collect(Collectors.toList());
		formObject.setRows(rows);

		return formObject;
	}

}
