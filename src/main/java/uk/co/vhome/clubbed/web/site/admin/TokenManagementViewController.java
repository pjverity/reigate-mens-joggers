package uk.co.vhome.clubbed.web.site.admin;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.co.vhome.clubbed.entities.UserDetailsEntity;
import uk.co.vhome.clubbed.entities.UserEntity;
import uk.co.vhome.clubbed.paymentmanagement.TokenManagementService;
import uk.co.vhome.clubbed.usermanagement.Group;
import uk.co.vhome.clubbed.usermanagement.UserAccountManagementService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TokenManagementViewController
{
	private final TokenManagementService tokenManagementService;

	private final UserAccountManagementService userAccountManagementService;

	public TokenManagementViewController(TokenManagementService tokenManagementService, UserAccountManagementService userAccountManagementService)
	{
		this.tokenManagementService = tokenManagementService;
		this.userAccountManagementService = userAccountManagementService;
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
				.forEach(r -> tokenManagementService.creditAccount(r.getUserDetailsEntity().getId(), r.getQuantity()));

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

		List<TokenManagementFormRow> rows = userAccountManagementService.enabledUsersInGroup(true, Group.MEMBER)
				                                    .stream()
				                                    .map(UserEntity::getUserDetailsEntity)
				                                    .sorted(UserDetailsEntity.LAST_NAME_FIRST_NAME_SORT)
				                                    .map(TokenManagementFormRow::new)
				                                    .collect(Collectors.toList());

		formObject.setRows(rows);

		return formObject;
	}

}
