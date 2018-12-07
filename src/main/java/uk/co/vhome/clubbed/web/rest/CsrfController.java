package uk.co.vhome.clubbed.web.rest;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Used to generate CSRF tokens ''on-demand'. This is required for the
 * login page which ties CSRF tokens to sessions. If a user doesn't log in
 * before the session expires, and then attempts to log in, the request will
 * be rejected due to the expiration and will be redirected (currently back to
 * the home page). The user is then forced to log in again.
 *
 * This can be avoided by requesting a fresh token at the point the user clicks
 * the submit button.
 */
@RestController
public class CsrfController {

	@GetMapping(value = "/rest/csrf")
	public CsrfToken csrf(CsrfToken token) {
		return token;
	}
}