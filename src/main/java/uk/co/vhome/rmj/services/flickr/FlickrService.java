package uk.co.vhome.rmj.services.flickr;

import org.springframework.security.access.annotation.Secured;
import uk.co.vhome.rmj.security.Role;
import uk.co.vhome.rmj.site.world.GalleryViewModelObject;

import java.util.Map;

/**
 * Basic Service used by Controllers to access Flickr images and info
 */
public interface FlickrService
{
	GalleryViewModelObject getPhotoUrlsForGroup(String groupNsid, Integer page);

	String getCurrentGroupNsid();

	String getCurrentGroupName();

	@Secured({Role.ADMIN})
	Map<String, String> groupsSearch(String searchText);

	@Secured({Role.ADMIN})
	void saveCurrentGroup(String name, String nsid);
}
