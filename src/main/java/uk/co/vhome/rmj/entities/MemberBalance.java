package uk.co.vhome.rmj.entities;

import uk.co.vhome.rmj.security.Group;

import javax.persistence.*;
import java.util.Objects;

/**
 * TODO: This is ugly and will become inefficient scales at O(n)
 * Represents a users balance
 */
@NamedNativeQuery(
		name = "AllEnabledMembersBalanceQuery",
		// cast() is required as PostgreSQL sum() function returns a bigint, which translates to a BigDecimal and so doesn't match
		// the ctor signature. {h-schema} is a Hibernate construct used for native queries, it substitutes in the default schema name
		// specified by the hibernate.default_schema JPA property. (This is done automatically for normal entity queries (HQL? JQL?))
		query = "SELECT u.id, u.first_name, u.last_name, u.username, cast(sum(p.quantity) as INTEGER) as balance FROM {h-schema}group_members gm, {h-schema}users u LEFT JOIN {h-schema}purchases p" +
				        " ON u.id = p.users_id" +
				        " WHERE u.enabled = TRUE" +
				        " AND u.username = gm.username AND gm.group_id = (SELECT id from groups where group_name = '" + Group.MEMBER + "')" +
				        " GROUP BY u.id, u.username, first_name, last_name",
		resultSetMapping = "QueryResultMapping"
)

@SqlResultSetMapping(
		name = "QueryResultMapping",
		classes = {
				@ConstructorResult(
					targetClass = MemberBalance.class,
					columns = {
							@ColumnResult(name = "id", type = Long.class),
							@ColumnResult(name = "username"),
							@ColumnResult(name = "first_name"),
							@ColumnResult(name = "last_name"),
							@ColumnResult(name = "balance")
					}
				)
		}
)
@MappedSuperclass
public class MemberBalance
{
	public static final String ALL_ENABLED_MEMBERS_BALANCE_QUERY = "AllEnabledMembersBalanceQuery";

	private final Long userId;

	private final String username;

	private final String firstName;

	private final String lastName;

	private final Integer balance;

	public MemberBalance(Long userId, String username, String firstName, String lastName, Integer balance)
	{
		this.userId = userId;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.balance = balance;
	}

	public Long getUserId()
	{
		return userId;
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

	public Integer getBalance()
	{
		return balance;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MemberBalance that = (MemberBalance) o;

		return Objects.equals(this.userId, that.userId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(userId);
	}
}
