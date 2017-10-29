package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Park> getAllParks() {
		List<Park> allParks = new ArrayList<>();
		
		String sqlAllParks = "SELECT * " +
							"FROM park";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllParks);
		
		while(results.next()) {
			Park thePark = new Park();
			
			thePark.setParkId(results.getLong("park_id"));
			thePark.setName(results.getString("name"));
			thePark.setLocation(results.getString("location"));
			thePark.setDateEstablished(results.getDate("establish_date").toLocalDate());
			thePark.setArea(results.getLong("area"));
			thePark.setVisitors(results.getLong("visitors"));
			thePark.setDescription(results.getString("description"));
			
			allParks.add(thePark);
		}
		
		return allParks;
	}

	@Override
	public Park getParkById(long parkId) {
		
		Park thePark = new Park();
		
		String sqlGetParkById = "SELECT * " +
								"FROM park " +
								"WHERE park_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetParkById, parkId);
		
		if(results.next()) {
			thePark.setParkId(results.getLong("park_id"));
			thePark.setName(results.getString("name"));
			thePark.setLocation(results.getString("location"));
			thePark.setDateEstablished(results.getDate("establish_date").toLocalDate());
			thePark.setArea(results.getLong("area"));
			thePark.setVisitors(results.getLong("visitors"));
			thePark.setDescription(results.getString("description"));
		}
		return thePark;
	}
}
