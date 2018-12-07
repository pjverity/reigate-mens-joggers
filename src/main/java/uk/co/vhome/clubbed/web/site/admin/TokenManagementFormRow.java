package uk.co.vhome.clubbed.web.site.admin;

import uk.co.vhome.clubbed.entities.UserDetailsEntity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class TokenManagementFormRow
{
	private UserDetailsEntity userDetailsEntity;

	@Min(value = 1, message = "{validation.constraint.Min.creditAmount}")
	@Max(value = 20, message = "{validation.constraint.Max.creditAmount}")
	private Long quantity;

	public TokenManagementFormRow(UserDetailsEntity userDetailsEntity)
	{
		this.userDetailsEntity = userDetailsEntity;
	}

	public UserDetailsEntity getUserDetailsEntity()
	{
		return userDetailsEntity;
	}

	public void setUserDetailsEntity(UserDetailsEntity userDetailsEntity)
	{
		this.userDetailsEntity = userDetailsEntity;
	}

	public Long getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Long quantity)
	{
		this.quantity = quantity;
	}

}
