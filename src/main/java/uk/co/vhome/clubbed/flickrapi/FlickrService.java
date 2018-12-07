package uk.co.vhome.clubbed.flickrapi;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.clubbed.usermanagement.Role;

/**
 * Basic Service used by Controllers to access Flickr images and info
 */
public interface FlickrService
{
	String IMAGE_URL_PATTERN = "https://farm{0}.staticflickr.com/{1}/{2}_{3}.jpg";

	PhotosResponse fetchPhotosForGroup(String groupNsid, Integer page);

	String getCurrentGroupNsid();

	String getCurrentGroupName();

	@Secured({Role.ADMIN})
	GroupsResponse groupsSearch(String searchText);

	@Secured({Role.ADMIN})
	boolean saveCurrentGroup(String name, String nsid);
}
