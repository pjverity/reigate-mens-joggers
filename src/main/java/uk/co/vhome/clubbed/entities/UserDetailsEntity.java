package uk.co.vhome.clubbed.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Entity representing additional information about users
 */
@Entity
@Table(name = "user_details")
public class UserDetailsEntity
{
	public static final Comparator<UserDetailsEntity> LAST_NAME_FIRST_NAME_SORT = Comparator.comparing(UserDetailsEntity::getLastName)
			                                                                              .thenComparing(UserDetailsEntity::getFirstName);

	@Id
	@Column(name = "users_id")
	private Long id;

	@OneToOne
	@MapsId
	@JoinColumn(name = "users_id")
	private UserEntity userEntity;

	@Basic(optional = false)
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Basic(optional = false)
	@Column(name = "last_name", nullable = false)
	private String lastName;

	private BigDecimal balance = BigDecimal.ZERO;

	@ManyToMany(mappedBy = "userDetailsEntities")
	private Set<Event> events;

	protected UserDetailsEntity()
	{
	}

	public UserDetailsEntity(UserEntity userEntity, String firstName, String lastName)
	{
		this.userEntity = userEntity;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public UserEntity getUserEntity()
	{
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity)
	{
		this.userEntity = userEntity;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public BigDecimal getBalance()
	{
		return balance;
	}

	public void setBalance(BigDecimal balance)
	{
		this.balance = balance;
	}

	public Set<Event> getEvents()
	{
		return events;
	}

	public void setEvents(Set<Event> events)
	{
		this.events = events;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserDetailsEntity that = (UserDetailsEntity) o;

		return Objects.equals(this.balance, that.balance) &&
				       Objects.equals(this.firstName, that.firstName) &&
				       Objects.equals(this.id, that.id) &&
				       Objects.equals(this.lastName, that.lastName) &&
				       Objects.equals(this.userEntity, that.userEntity);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(balance, firstName, id, lastName, userEntity);
	}


	@Override
	public String toString()
	{
		return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
				       .add("balance = " + balance)
				       .add("firstName = " + firstName)
				       .add("id = " + id)
				       .add("lastName = " + lastName)
				       .add("userEntity = " + userEntity)
				       .toString();
	}
}
