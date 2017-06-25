package uk.co.vhome.rmj.site.world;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import uk.co.vhome.rmj.services.flickr.FlickrService;

import javax.inject.Inject;

@Controller
public class GalleryViewController
{
	private static final String VIEW_NAME = "/world/gallery";

	private final FlickrService flickrService;

	@Inject
	public GalleryViewController(FlickrService flickrService)
	{
		this.flickrService = flickrService;
	}

	@GetMapping(VIEW_NAME)
	public GalleryViewModelObject get()
	{
		return flickrService.getPhotoUrlsForGroup(currentGroupNsid(), 1);
	}

	@GetMapping(VIEW_NAME + "/{groupNsid}/{page}")
	public ModelAndView get(@PathVariable("groupNsid") String groupNsid, @PathVariable("page") Integer page)
	{
		ModelAndView modelAndView = new ModelAndView(VIEW_NAME);

		if ( !currentGroupNsid().equalsIgnoreCase(groupNsid) )
		{
			return new ModelAndView(new RedirectView(VIEW_NAME, true));
		}

		modelAndView.addObject(flickrService.getPhotoUrlsForGroup(groupNsid, page));

		return modelAndView;
	}

	@ModelAttribute("currentGroupNsid")
	public String currentGroupNsid()
	{
		return flickrService.getCurrentGroupNsid();
	}
}
