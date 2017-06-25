package uk.co.vhome.rmj.services.flickr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A POJO representation of a response message returned from making a query for Flickr Photos
 */
public class PhotosResponse
{
	public static class Photo
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

		public String getId()
		{
			return id;
		}

		public String getOwner()
		{
			return owner;
		}

		public String getSecret()
		{
			return secret;
		}

		public String getServer()
		{
			return server;
		}

		public Integer getFarm()
		{
			return farm;
		}

		public String getTitle()
		{
			return title;
		}

		public Boolean isPublic()
		{
			return _public;
		}

		public Boolean getFriend()
		{
			return friend;
		}

		public Boolean getFamily()
		{
			return family;
		}

		public String getOwnerName()
		{
			return ownerName;
		}

		public String getDateAdded()
		{
			return dateAdded;
		}
	}

	public static class PhotoCollectionInfo
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

		public Integer getPage()
		{
			return page;
		}

		public Integer getPages()
		{
			return pages;
		}

		public Integer getPerPage()
		{
			return perPage;
		}

		public String getTotal()
		{
			return total;
		}

		public List<Photo> getPhotos()
		{
			return photos;
		}
	}

	@JsonProperty("photos")
	PhotoCollectionInfo photoCollectionInfo;

	@JsonProperty("stat")
	String status;

	public PhotoCollectionInfo getPhotoCollectionInfo()
	{
		return photoCollectionInfo;
	}

	public String getStatus()
	{
		return status;
	}
}
