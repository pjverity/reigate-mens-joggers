package uk.co.vhome.rmj.site.world;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * Created by paulverity on 25/06/2017.
 */
@Component
public class GalleryViewModelObject
{
	private String galleryName;

	private int currentPage;

	private int imagesPerPage;

	private int totalPages;

	private Set<String> imageUrls = Collections.emptySet();

	public String getGalleryName()
	{
		return galleryName;
	}

	public void setGalleryName(String galleryName)
	{
		this.galleryName = galleryName;
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public int getImagesPerPage()
	{
		return imagesPerPage;
	}

	public void setImagesPerPage(int imagesPerPage)
	{
		this.imagesPerPage = imagesPerPage;
	}

	public int getTotalPages()
	{
		return totalPages;
	}

	public void setTotalPages(int totalPages)
	{
		this.totalPages = totalPages;
	}

	public Set<String> getImageUrls()
	{
		return imageUrls;
	}

	public void setImageUrls(Set<String> imageUrls)
	{
		this.imageUrls = imageUrls;
	}
}
