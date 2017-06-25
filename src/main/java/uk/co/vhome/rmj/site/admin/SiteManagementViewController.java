package uk.co.vhome.rmj.site.admin;

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
	public void setGroupId(@RequestParam("groupName") String groupName, @RequestParam("groupId") String groupId)
	{
		flickrService.saveCurrentGroup(groupName, groupId);
	}

	@GetMapping("/admin/flickr/searchGroups")
	@ResponseBody
	public Map<String, String> flickerGroupsSearch(@RequestParam("searchText") String searchText)
	{
		GroupsResponse groupsResponse = flickrService.groupsSearch(searchText);

		if (groupsResponse == null)
		{
			return Collections.emptyMap();
		}

		return groupsResponse.getGroupCollectionInfo().getGroups().stream()
				       .collect(Collectors.toMap(GroupsResponse.Group::getName, GroupsResponse.Group::getNsid));

	}
}
