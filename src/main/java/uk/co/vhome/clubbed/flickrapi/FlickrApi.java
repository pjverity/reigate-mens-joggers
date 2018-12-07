package uk.co.vhome.clubbed.flickrapi;

/**
 * Interface to a subset of the Flickr API
 */
public interface FlickrApi
{
	PhotosResponse groupsPoolsGetPhotos(String groupNsid, Integer page);

	GroupsResponse groupsSearch(String groupName);
}
