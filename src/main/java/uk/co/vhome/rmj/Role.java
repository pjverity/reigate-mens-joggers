package uk.co.vhome.rmj;

import javax.annotation.security.DeclareRoles;

/**
 * Names of Authority roles used in the application
 */
@DeclareRoles({"SYSTEM","ADMIN","MEMBER","ORGANISER"})
public interface Role
{
	String SYSTEM = "ROLE_SYSTEM";

	String RUN_AS_SYSTEM = "ROLE_RUN_AS_SYSTEM";

	String ADMIN = "ROLE_ADMIN";

	String ORGANISER = "ROLE_ORGANISER";

	String MEMBER = "ROLE_MEMBER";

	String ANON = "ROLE_ANON";
}
