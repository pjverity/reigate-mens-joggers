package uk.co.vhome.rmj.site.admin;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.co.vhome.rmj.entities.MemberBalance;
import uk.co.vhome.rmj.services.TokenManagementService;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TokenManagementViewController
{
	private static final Map<Integer, String> CREDIT_QUANTITIES = new LinkedHashMap<>();

	private final TokenManagementService tokenManagementService;

	public TokenManagementViewController(TokenManagementService tokenManagementService)
	{
		this.tokenManagementService = tokenManagementService;
	}

	static
	{
		/*
		 * Take note of limits defined in DefaultTokenManagementService
		 */
		CREDIT_QUANTITIES.put(null, "None");
		CREDIT_QUANTITIES.put(1, "1");
		CREDIT_QUANTITIES.put(2, "2");
		CREDIT_QUANTITIES.put(3, "3");
		CREDIT_QUANTITIES.put(4, "4");
		CREDIT_QUANTITIES.put(5, "5");
		CREDIT_QUANTITIES.put(6, "6");
		CREDIT_QUANTITIES.put(7, "7");
		CREDIT_QUANTITIES.put(8, "8");
		CREDIT_QUANTITIES.put(9, "9");
		CREDIT_QUANTITIES.put(10, "10");
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
				.forEach(r -> adjustBalance(r.getMemberBalance().getUserId(), r.getQuantity()));

		return "redirect:/admin/token-management";
	}

	private void adjustBalance(Long userId, int quantity)
	{
		tokenManagementService.creditAccount(userId, quantity);
	}

	@GetMapping(value = "/admin/token-management")
	void get()
	{
	}

	@SuppressWarnings("unused")
	@ModelAttribute("creditQuantities")
	Map<Integer, String> creditQuantities()
	{
		return CREDIT_QUANTITIES;
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
