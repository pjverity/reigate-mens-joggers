package uk.co.vhome.rmj.services.flickr;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.rmj.site.world.GalleryViewModelObject;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Basic implementation of the {@link FlickrService} for obtaining information from Flickr and
 * accessing persistent information from the DB
 */
@Service
public class DefaultFlickrService implements FlickrService
{
	private static final String IMAGE_URL_PATTERN = "https://farm{0}.staticflickr.com/{1}/{2}_{3}.jpg";

	private static final String SQL_SELECT_GROUP_NAME = "SELECT value from site_settings WHERE name = 'FLICKR_GROUP_NAME'";

	private static final String SQL_SELECT_GROUP_NSID = "SELECT value from site_settings WHERE name = 'FLICKR_GROUP_NSID'";

	private static final String SQL_UPDATE_GROUP_NAME = "UPDATE site_settings SET value = ? WHERE name = 'FLICKR_GROUP_NAME'";

	private static final String SQL_UPDATE_GROUP_NSID = "UPDATE site_settings SET value = ? WHERE name = 'FLICKR_GROUP_NSID'";

	private final FlickrApi flickrApi;

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public DefaultFlickrService(FlickrApi flickrApi, DataSource dataSource)
	{
		this.flickrApi = flickrApi;
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public GalleryViewModelObject getPhotoUrlsForGroup(String groupNsid, Integer page)
	{
		PhotosResponse photosResponse = flickrApi.groupsPoolsGetPhotos(groupNsid, page);

		GalleryViewModelObject galleryViewModelObject = new GalleryViewModelObject();

		if (photosResponse != null)
		{
			galleryViewModelObject.setImageUrls(photosResponse.photoCollectionInfo.photos.stream()
					       .map(this::toPhotoUrl)
					       .collect(Collectors.toSet()));

			galleryViewModelObject.setTotalPages(photosResponse.photoCollectionInfo.pages);
			galleryViewModelObject.setImagesPerPage(photosResponse.photoCollectionInfo.perPage);
			galleryViewModelObject.setCurrentPage(photosResponse.photoCollectionInfo.page);
			galleryViewModelObject.setGalleryName(getCurrentGroupName());
		}

		return galleryViewModelObject;
	}

	@Override
	public Map<String, String> groupsSearch(String searchText)
	{
		GroupsResponse response = flickrApi.groupsSearch(searchText);

		if (response == null)
		{
			return Collections.emptyMap();
		}

		return response.groupCollectionInfo.groups.stream()
				       .collect(Collectors.toMap(group -> group.name, group -> group.nsid));
	}

	@Override
	public String getCurrentGroupNsid()
	{
		return jdbcTemplate.queryForObject(SQL_SELECT_GROUP_NSID, String.class);
	}

	@Override
	public String getCurrentGroupName()
	{
		return jdbcTemplate.queryForObject(SQL_SELECT_GROUP_NAME, String.class);
	}

	@Override
	@Transactional
	public void saveCurrentGroup(String groupName, String groupNsid)
	{
		jdbcTemplate.update(SQL_UPDATE_GROUP_NAME, groupName);
		jdbcTemplate.update(SQL_UPDATE_GROUP_NSID, groupNsid);
	}

	private String toPhotoUrl(PhotosResponse.Photo photo)
	{
		return MessageFormat.format(IMAGE_URL_PATTERN, photo.farm, photo.server, photo.id, photo.secret);
	}

}
