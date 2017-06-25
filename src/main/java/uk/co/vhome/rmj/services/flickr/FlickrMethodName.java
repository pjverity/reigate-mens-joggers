package uk.co.vhome.rmj.services.flickr;

/**
 * Subset of the Flickr API methods
 */
public enum FlickrMethodName
{
	GROUPS_POOLS_GET_PHOTOS("flickr.groups.pools.getPhotos"),

	GROUPS_SEARCH("flickr.groups.search");

	private final String methodName;

	FlickrMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	public String getMethodName()
	{
		return methodName;
	}

}
