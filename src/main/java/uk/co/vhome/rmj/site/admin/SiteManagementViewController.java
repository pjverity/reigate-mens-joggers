package uk.co.vhome.rmj.site.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.co.vhome.rmj.services.flickr.FlickrService;
import uk.co.vhome.rmj.services.flickr.GroupsResponse;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SiteManagementViewController
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final FlickrService flickrService;

	@Inject
	public SiteManagementViewController(FlickrService flickrService)
	{
		this.flickrService = flickrService;
	}

	@GetMapping("/admin/site-management")
	public void get()
	{
	}

	@ModelAttribute(name = "nsid")
	public String getGroupId()
	{
		return flickrService.getCurrentGroupNsid();
	}

	@ModelAttribute(name = "groupName")
	public String getGroupName()
	{
		return flickrService.getCurrentGroupName();
	}

	@PostMapping(value = "/admin/flickr/saveGroupNsid")
	@ResponseBody
	public boolean saveSelectedFlickrGroup(@RequestParam("groupName") String groupName, @RequestParam("groupId") String groupId)
	{
		if (groupName.isEmpty() || groupId.isEmpty())
		{
			LOGGER.warn("Failed to save incomplete group information. groupName: {}, groupId: {}", groupName, groupId);
			return false;
		}

		return flickrService.saveCurrentGroup(groupName, groupId);
	}

	@GetMapping("/admin/flickr/searchGroups")
	@ResponseBody
	public Map<String, String> flickerGroupsSearch(@RequestParam("searchText") String searchText)
	{
		if ( searchText.isEmpty() )
		{
			return Collections.emptyMap();
		}

		GroupsResponse groupsResponse = flickrService.groupsSearch(searchText);

		if (groupsResponse == null)
		{
			return Collections.emptyMap();
		}

		return groupsResponse.getGroupCollectionInfo().getGroups().stream()
				       .collect(Collectors.toMap(GroupsResponse.Group::getName, GroupsResponse.Group::getNsid));

	}
}
