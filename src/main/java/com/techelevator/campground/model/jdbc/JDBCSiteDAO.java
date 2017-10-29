package com.techelevator.campground.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {

private JdbcTemplate jdbcTemplate;
	
	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<Site> getSitesBySpecifics(long campgroundId, LocalDate fromDate, LocalDate toDate, long maxOcc, long rvLength, boolean accessible, boolean utilities) {
		List<Site> siteDetails = new ArrayList<>();
		
		String sqlSiteDetails = "SELECT * " +
								"FROM site " +
								"WHERE campground_id = ? AND max_occupancy = ? AND max_rv_length = ? AND accessible = ? AND utilities = ? " +
								"AND site_id NOT IN ( " +
									"SELECT site_id " + 
									"FROM reservation " + 
									"WHERE (? BETWEEN from_date AND to_date)" + 
									"AND (? BETWEEN from_date AND to_date)" +
									")";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSiteDetails, campgroundId, maxOcc, rvLength, accessible, utilities, fromDate, toDate);
								
		while(results.next()) {
			Site theSite = new Site();
			
			theSite.setSiteId(results.getLong("site_id"));
			theSite.setCampgroundId(results.getLong("campground_id"));
			theSite.setSiteNumber(results.getLong("site_number"));
			theSite.setMaxOccupancy(results.getLong("max_occupancy"));
			theSite.setAccessible(results.getBoolean("accessible"));
			theSite.setMaxLengthRV(results.getLong("max_rv_length"));
			theSite.setUtilities(results.getBoolean("utilities"));
			
			siteDetails.add(theSite);
		}
		return siteDetails;
	}
	
	
	public List<Site> getAvailableSitesByPark(long parkId, LocalDate fromDate, LocalDate toDate) {
		List<Site> getSitesByPark = new ArrayList<>();
		
		String sqlAvailSites = "SELECT campground.name, site.site_id, site.occupancy, site.accessible, site.max_rv_length, site.utilities, campground.daily_fee " +
								"FROM site " +
								"JOIN campground ON site.campground_id = campground.campground_id " +
								"JOIN park ON campground.park_id = park.park_id " +
								"WHERE park.park_id = ? AND (? NOT BETWEEN from_date AND to_date) AND (? NOT BETWEEN from_date AND to_date)";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAvailSites, parkId, fromDate, toDate);
		
		while(results.next()) {
			Site theSite = new Site();
			
			theSite.setSiteId(results.getLong("site_id"));
			theSite.setCampgroundId(results.getLong("campground_id"));
			theSite.setSiteNumber(results.getLong("site_number"));
			theSite.setMaxOccupancy(results.getLong("max_occupancy"));
			theSite.setAccessible(results.getBoolean("accessible"));
			theSite.setMaxLengthRV(results.getLong("max_rv_length"));
			theSite.setUtilities(results.getBoolean("utilities"));
			
			getSitesByPark.add(theSite);
		}
		return getSitesByPark;
	}
	
	
	@Override
	public List<Site> getAvailableSites(long campgroundId, LocalDate fromDate, LocalDate toDate) {
		List<Site> availSites = new ArrayList<>();
		
		String sqlAvailSites = "SELECT * " +
								"FROM site " +
								"WHERE campground_id = ? AND site_id NOT IN (" +
									"SELECT site_id " +
									"FROM reservation " +
									"WHERE (? BETWEEN from_date AND to_date) " +
									"AND (? BETWEEN from_date AND to_date)" +
									")";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAvailSites, campgroundId, fromDate, toDate);
		
		while(results.next()) {
			Site theSite = new Site();
			
			theSite.setSiteId(results.getLong("site_id"));
			theSite.setCampgroundId(results.getLong("campground_id"));
			theSite.setSiteNumber(results.getLong("site_number"));
			theSite.setMaxOccupancy(results.getLong("max_occupancy"));
			theSite.setAccessible(results.getBoolean("accessible"));
			theSite.setMaxLengthRV(results.getLong("max_rv_length"));
			theSite.setUtilities(results.getBoolean("utilities"));
			
			availSites.add(theSite);
		}
		return availSites;
	}

}
