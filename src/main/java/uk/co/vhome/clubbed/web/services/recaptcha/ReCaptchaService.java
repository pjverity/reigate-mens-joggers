package uk.co.vhome.clubbed.web.services.recaptcha;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Injectable service interface use to validate users are human when signing up
 */
@Validated
public interface ReCaptchaService
{
	boolean isResponseValid(@NotBlank(message = "{validation.constraint.Blank.recaptchaResponse}") String response,
	                        String remoteIp);
}
