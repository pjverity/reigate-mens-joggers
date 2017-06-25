package uk.co.vhome.rmj.services.flickr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A POJO representation of a response message returned from making a query for Flicker Group information
 */
class GroupsResponse
{
	static class Group
	{
		@JsonProperty("nsid")
		String nsid;

		@JsonProperty("name")
		String name;

		@JsonProperty("eighteenplus")
		Boolean eighteenPlus;
	}

	static class GroupCollectionInfo
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
	}

	@JsonProperty("groups")
	GroupCollectionInfo groupCollectionInfo;

	@JsonProperty("stat")
	String status;
}
