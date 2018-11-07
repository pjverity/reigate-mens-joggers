package uk.co.vhome.clubbed.flickrapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * Basic implementation of the {@link FlickrService} for obtaining information from Flickr and
 * accessing persistent information from the DB
 */
@Service
public class DefaultFlickrService implements FlickrService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String SQL_SELECT_GROUP_NAME = "SELECT value from site_settings WHERE name = 'FLICKR_GROUP_NAME'";

	private static final String SQL_SELECT_GROUP_NSID = "SELECT value from site_settings WHERE name = 'FLICKR_GROUP_NSID'";

	private static final String SQL_UPDATE_GROUP_NAME = "UPDATE site_settings SET value = ? WHERE name = 'FLICKR_GROUP_NAME'";

	private static final String SQL_UPDATE_GROUP_NSID = "UPDATE site_settings SET value = ? WHERE name = 'FLICKR_GROUP_NSID'";

	private static final String SQL_INSERT_GROUP_NAME = "INSERT INTO site_settings VALUES(DEFAULT, 'FLICKR_GROUP_NAME', ?)";

	private static final String SQL_INSERT_GROUP_NSID = "INSERT INTO site_settings VALUES(DEFAULT, 'FLICKR_GROUP_NSID', ?)";

	private final FlickrApi flickrApi;

	private final JdbcTemplate jdbcTemplate;

	@Autowired
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
		try
		{
			return jdbcTemplate.queryForObject(SQL_SELECT_GROUP_NSID, String.class);
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			LOGGER.error("Failed to retrieve groupNsid", e.getMessage());
			return "?";
		}
	}

	@Override
	public String getCurrentGroupName()
	{
		try
		{
			return jdbcTemplate.queryForObject(SQL_SELECT_GROUP_NAME, String.class);
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			LOGGER.error("Failed to retrieve groupName", e.getMessage());
			return "Not Found";
		}
	}

	@Override
	@Transactional
	public boolean saveCurrentGroup(String groupName, String groupNsid)
	{
		if (jdbcTemplate.update(SQL_UPDATE_GROUP_NAME, groupName) == 0)
		{
			if ( jdbcTemplate.update(SQL_INSERT_GROUP_NAME, groupName) == 0 )
			{
				LOGGER.error("Failed to update groupName");
				return false;
			}
		}

		if ( jdbcTemplate.update(SQL_UPDATE_GROUP_NSID, groupNsid) == 0 )
		{
			if ( jdbcTemplate.update(SQL_INSERT_GROUP_NSID, groupNsid) == 0 )
			{
				LOGGER.error("Failed to update groupNsid");
				return false;
			}
		}

		return true;
	}

}
