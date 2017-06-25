package uk.co.vhome.rmj.services.flickr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A POJO representation of a response message returned from making a query for Flicker Group information
 */
public class GroupsResponse
{
	public static class Group
	{
		@JsonProperty("nsid")
		String nsid;

		@JsonProperty("name")
		String name;

		@JsonProperty("eighteenplus")
		Boolean eighteenPlus;

		public String getNsid()
		{
			return nsid;
		}

		public String getName()
		{
			return name;
		}

		public Boolean getEighteenPlus()
		{
			return eighteenPlus;
		}
	}

	public static class GroupCollectionInfo
	{
		@JsonProperty("page")
		Integer page;

		@JsonProperty("pages")
		Integer pages;

		@JsonProperty("perpage")
		Integer perPage;

		@JsonProperty("total")
		String total;

		@JsonProperty("group")
		List<Group> groups;

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

		public List<Group> getGroups()
		{
			return groups;
		}
	}

	@JsonProperty("groups")
	GroupCollectionInfo groupCollectionInfo;

	@JsonProperty("stat")
	String status;

	public GroupCollectionInfo getGroupCollectionInfo()
	{
		return groupCollectionInfo;
	}

	public String getStatus()
	{
		return status;
	}
}
