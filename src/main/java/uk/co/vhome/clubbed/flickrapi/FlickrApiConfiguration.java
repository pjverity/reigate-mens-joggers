package uk.co.vhome.clubbed.flickrapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FlickrApiConfiguration
{
	@Bean
	public RestTemplate flickrRestTemplate(@Value("${flickr.api-key}") String apiKey)
	{

		RestTemplate restTemplate = new RestTemplate();

		Map<String, String> defaultVariables = new HashMap<>();
		defaultVariables.put("apiKey", apiKey);

		restTemplate.setDefaultUriVariables(defaultVariables);

		return restTemplate;
	}
}
