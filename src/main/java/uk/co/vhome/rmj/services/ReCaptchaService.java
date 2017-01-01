package uk.co.vhome.rmj.services;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * Injectable service interface use to validate users are human when signing up
 */
@Validated
public interface ReCaptchaService
{
	boolean isResponseValid(@NotBlank(message = "{validation.constraint.Blank.recaptchaResponse}") String response,
	                        String remoteIp);
}
