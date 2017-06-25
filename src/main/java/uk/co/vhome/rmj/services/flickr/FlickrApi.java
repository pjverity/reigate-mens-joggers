package uk.co.vhome.rmj.services.flickr;

/**
 * Interface to a subset of the Flickr API
 */
public interface FlickrApi
{
	PhotosResponse groupsPoolsGetPhotos(String groupNsid, Integer page);

	GroupsResponse groupsSearch(String groupName);
}
