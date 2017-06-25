package uk.co.vhome.rmj.services.flickr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A POJO representation of a response message returned from making a query for Flickr Photos
 */
class PhotosResponse
{
	static class Photo
	{
		@JsonProperty("id")
		String id;

		@JsonProperty("owner")
		String owner;

		@JsonProperty("secret")
		String secret;

		@JsonProperty("server")
		String server;

		@JsonProperty("farm")
		Integer farm;

		@JsonProperty("title")
		String title;

		@JsonProperty("ispublic")
		Boolean _public;

		@JsonProperty("isfriend")
		Boolean friend;

		@JsonProperty("isfamily")
		Boolean family;

		@JsonProperty("ownername")
		String ownerName;

		@JsonProperty("dateadded")
		String dateAdded;
	}

	static class PhotoCollectionInfo
	{
		@JsonProperty("page")
		Integer page;

		@JsonProperty("pages")
		Integer pages;

		@JsonProperty("perpage")
		Integer perPage;

		@JsonProperty("total")
		String total;

		@JsonProperty("photo")
		List<Photo> photos;
	}

	@JsonProperty("photos")
	PhotoCollectionInfo photoCollectionInfo;

	@JsonProperty("stat")
	String status;
}
