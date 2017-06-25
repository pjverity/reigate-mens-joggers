package uk.co.vhome.rmj.services.flickr;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Basic implementation of the {@link FlickrService} for obtaining information from Flickr and
 * accessing persistent information from the DB
 */
@Service
public class DefaultFlickrService implements FlickrService
{
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
	public PhotosResponse fetchPhotosForGroup(String groupNsid, Integer page)
	{
		return flickrApi.groupsPoolsGetPhotos(groupNsid, page);
	}

	@Override
	public GroupsResponse groupsSearch(String searchText)
	{
		return flickrApi.groupsSearch(searchText);
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

}
