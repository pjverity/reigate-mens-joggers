package uk.co.vhome.rmj.site.world;

import uk.co.vhome.rmj.services.flickr.PhotosResponse;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static uk.co.vhome.rmj.services.flickr.FlickrService.IMAGE_URL_PATTERN;

public class GalleryViewModelObject
{
	private String galleryName;

	private int currentPage;

	private int imagesPerPage;

	private int totalPages;

	private Set<String> imageUrls = Collections.emptySet();

	public GalleryViewModelObject()
	{
	}

	public GalleryViewModelObject(PhotosResponse photosResponse, String groupName)
	{
		if (photosResponse != null)
		{
			imageUrls = photosResponse.getPhotoCollectionInfo().getPhotos().stream()
					            .map(this::toPhotoUrl)
					            .collect(Collectors.toSet());

			currentPage = photosResponse.getPhotoCollectionInfo().getPage();
			totalPages = photosResponse.getPhotoCollectionInfo().getPages();
			imagesPerPage = photosResponse.getPhotoCollectionInfo().getPerPage();
			galleryName = groupName;
		}
	}

	private String toPhotoUrl(PhotosResponse.Photo photo)
	{
		return MessageFormat.format(IMAGE_URL_PATTERN, photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret());
	}

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
