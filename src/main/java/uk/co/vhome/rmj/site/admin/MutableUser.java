package uk.co.vhome.rmj.site.admin;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Simple wrapper around existing immutable {@link UserDetails} to record changes
 */
public class MutableUser
{
	private boolean enabled;

	private UserDetails userDetails;

	public MutableUser()
	{
	}

	MutableUser(UserDetails userDetails)
	{
		this.userDetails = userDetails;
		this.enabled = userDetails.isEnabled();
	}

	public UserDetails getUserDetails()
	{
		return userDetails;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	User mergeWith(UserDetails user)
	{
		return new User(user.getUsername(),
		                user.getPassword(),
		                enabled,
		                user.isAccountNonExpired(),
		                user.isCredentialsNonExpired(),
		                user.isAccountNonLocked(),
		                user.getAuthorities());
	}
}
