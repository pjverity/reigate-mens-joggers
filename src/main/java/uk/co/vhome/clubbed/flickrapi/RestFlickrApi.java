package uk.co.vhome.clubbed.flickrapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static uk.co.vhome.clubbed.flickrapi.FlickrMethodName.GROUPS_POOLS_GET_PHOTOS;
import static uk.co.vhome.clubbed.flickrapi.FlickrMethodName.GROUPS_SEARCH;

/**
 * REST implementation of the {@link FlickrApi} interface
 */
@Component
public class RestFlickrApi implements FlickrApi
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String FLICKR_REST_API_URL = "https://api.flickr.com/services/rest";

	private final RestTemplate restTemplate;

	private final UriComponentsBuilder build = UriComponentsBuilder.fromUriString(FLICKR_REST_API_URL)
			                                           .query("method={methodName}")
			                                           .query("api_key={apiKey}")
			                                           .query("nojsoncallback=1")
			                                           .query("format=json");

	@Autowired
	public RestFlickrApi(RestTemplate flickrRestTemplate)
	{
		restTemplate = flickrRestTemplate;
	}

	@Override
	public PhotosResponse groupsPoolsGetPhotos(String groupNsid, Integer page)
	{
		UriComponentsBuilder uriBuilder = build.cloneBuilder()
				                                    .query("group_id={groupNsid}")
				                                    .query("page={page}")
				                                    .query("per_page=20");

		Map<String, Object> parameterArgs = new HashMap<>();
		parameterArgs.put("groupNsid", groupNsid);
		parameterArgs.put("page", page);

		return callMethod(GROUPS_POOLS_GET_PHOTOS, parameterArgs, uriBuilder, PhotosResponse.class);

	}

	@Override
	public GroupsResponse groupsSearch(String searchText)
	{
		UriComponentsBuilder uriBuilder = build.cloneBuilder()
				                                    .query("text={searchText}");

		Map<String, Object> parameterArgs = new HashMap<>();
		parameterArgs.put("searchText", searchText);

		return callMethod(GROUPS_SEARCH, parameterArgs, uriBuilder, GroupsResponse.class);
	}

	private <T> T callMethod(FlickrMethodName methodName, Map<String, Object> parameterArgs, UriComponentsBuilder uriBuilder, Class<T> responseType)
	{
		parameterArgs.put("methodName", methodName.getMethodName());

		ResponseEntity<T> response = restTemplate.getForEntity(uriBuilder.build().toUriString(), responseType, parameterArgs);

		if (response.getStatusCode() == HttpStatus.OK)
		{
			return response.getBody();
		}

		LOGGER.error("{} call failed with response code: {}", methodName.getMethodName(), response.getStatusCode());

		return null;
	}
}
