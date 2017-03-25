package uk.co.vhome.rmj.site.admin;

import org.springframework.beans.TypeMismatchException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

	@ExceptionHandler(TypeMismatchException.class)
	public void typeMismatchExceptionHandler(TypeMismatchException e)
	{

	}

	@PostMapping(value = "/admin/token-management")
	String post(@Valid TokenManagementFormObject formObject, BindingResult bindingResult)
	{
		if ( bindingResult.hasErrors() )
		{
			return "/admin/token-management";
		}

		formObject.getRows().stream()
				.filter(r -> r.getQuantity() != 0)
				.forEach(r -> adjustBalance(r.getMemberBalance().getUsername(), r.getQuantity()));

		return "redirect:/admin/token-management";
	}

	private void adjustBalance(String username, int quantity)
	{
		tokenManagementService.adjustBalance(username, quantity);
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
