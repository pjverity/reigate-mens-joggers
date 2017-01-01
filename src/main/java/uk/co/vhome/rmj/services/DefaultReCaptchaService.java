package uk.co.vhome.rmj.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
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

	@Value("${recaptcha.url}")
	private String recaptchaUrl;

	@Value("${recaptcha.secret-key}")
	private String recaptchaSecretKey;

	@Inject
	public DefaultReCaptchaService(RestTemplate restTemplate)
	{
		this.restTemplate = restTemplate;
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
		RecaptchaResponse recaptchaResponse = restTemplate.postForObject(recaptchaUrl, requestParameters, RecaptchaResponse.class);

		return recaptchaResponse.success;
	}
}
