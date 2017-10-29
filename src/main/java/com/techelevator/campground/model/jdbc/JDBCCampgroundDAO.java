package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {

private JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Campground> getAllCampgroundsByPark(long parkId) {
		List<Campground> parkCampgrounds = new ArrayList<>();
		
		String sqlCampgroundsByPark = "SELECT * " +
									"FROM campground " +
									"WHERE park_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlCampgroundsByPark, parkId);
		
		while (results.next()) {
		Campground theCampground = new Campground();
		
		theCampground.setCampgroundId(results.getLong("campground_id"));
		theCampground.setParkId(results.getLong("park_id"));
		theCampground.setName(results.getString("name"));
		theCampground.setOpenDate(results.getString("open_from_mm"));
		theCampground.setCloseDate(results.getString("open_to_mm"));
		theCampground.setDailyFee(results.getDouble("daily_fee"));
		
		parkCampgrounds.add(theCampground);
		}
		
		return parkCampgrounds;
	}

}
