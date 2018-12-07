package uk.co.vhome.clubbed.web.services.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

/**
 * Implementation of {@link ReCaptchaService} used to verify if the user human
 */
@Service
public class DefaultReCaptchaService implements ReCaptchaService
{

	@SuppressWarnings("all")
	private static class RecaptchaResponse {
		@JsonProperty("success")
		private boolean success;
		@JsonProperty("error-codes")
		private Collection<String> errorCodes;

		@Override
		public String toString()
		{
			return "RecaptchaResponse{" +
					"success=" + success +
					", errorCodes=" + errorCodes +
					'}';
		}
	}

	private final RestTemplate restTemplate;

	private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";

	private final String recaptchaSecretKey;

	@Autowired
	public DefaultReCaptchaService(RestTemplate restTemplate, @Value("${recaptcha.secret-key}") String recaptchaSecretKey)
	{
		this.restTemplate = restTemplate;
		this.recaptchaSecretKey = recaptchaSecretKey;
	}

	@Override
	public boolean isResponseValid(String response, String remoteIp)
	{
		// NOTE: This HAS to be a LinkedMultiValueMap, a standard LinkedHashMap does not work!
		MultiValueMap<String, Object> requestParameters = new LinkedMultiValueMap<>();
		requestParameters.add("secret", recaptchaSecretKey);
		requestParameters.add("response", response);
		requestParameters.add("remoteip", remoteIp);

		// NOTE: The request is sent by encoding the parameters in the URL, it is NOT a JSON request:
		// https://groups.google.com/forum/#!msg/recaptcha/yEPN3d9ylT8/j6QW8G-vGgAJ;context-place=searchin/recaptcha/post$20url
		RecaptchaResponse recaptchaResponse = restTemplate.postForObject(RECAPTCHA_URL, requestParameters, RecaptchaResponse.class);

		return recaptchaResponse.success;
	}
}
