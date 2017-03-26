package uk.co.vhome.rmj.site.admin;

import javax.validation.Valid;
import java.util.List;

public class TokenManagementFormObject
{
	@Valid
	private List<TokenManagementFormRow> rows;

	public List<TokenManagementFormRow> getRows()
	{
		return rows;
	}

	public void setRows(List<TokenManagementFormRow> rows)
	{
		this.rows = rows;
	}
}
