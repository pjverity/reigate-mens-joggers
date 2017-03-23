package uk.co.vhome.rmj.entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents a users balance
 */
@NamedNativeQuery(
		name = "AllEnabledMembersBalanceQuery",
		// cast() is required as PostgreSQL sum() function returns a bigint, which translates to a BigDecimal and so doesn't match
		// the ctor signature. {h-schema} is a Hibernate construct used for native queries, it substitutes in the default schema name
		// specified by the hibernate.default_schema JPA property. (This is done automatically for normal entity queries (HQL? JQL?))
		query = "SELECT u.username, ud.first_name, ud.last_name, cast(sum(p.quantity) as INTEGER) as quantity FROM {h-schema}users u, {h-schema}user_details ud, {h-schema}purchases p" +
				        " WHERE ud.username = p.username" +
				        " AND u.username = p.username" +
				        " AND u.enabled = TRUE" +
				        " GROUP BY u.username, first_name, last_name",
		resultSetMapping = "QueryResultMapping"
)

@SqlResultSetMapping(
		name = "QueryResultMapping",
		classes = {
				@ConstructorResult(
					targetClass = MemberBalance.class,
					columns = {
							@ColumnResult(name = "username"),
							@ColumnResult(name = "first_name"),
							@ColumnResult(name = "last_name"),
							@ColumnResult(name = "quantity")
					}
				)
		}
)
@MappedSuperclass
public class MemberBalance
{
	public static final String ALL_ENABLED_MEMBERS_BALANCE_QUERY = "AllEnabledMembersBalanceQuery";

	private final String username;

	private final String firstName;

	private final String lastName;

	private final Integer quantity;

	public MemberBalance(String username, String firstName, String lastName, Integer quantity)
	{
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.quantity = quantity;
	}

	public String getUsername()
	{
		return username;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MemberBalance that = (MemberBalance) o;

		return Objects.equals(this.username, that.username);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(username);
	}
}
