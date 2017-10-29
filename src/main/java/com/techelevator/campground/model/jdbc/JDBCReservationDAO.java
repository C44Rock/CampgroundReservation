package com.techelevator.campground.model.jdbc;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO{

private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Reservation makeNewReservation(long siteId, String name, LocalDate fromDate, LocalDate toDate) {
		String sqlGetNextId = "SELECT nextval('reservation_reservation_id_seq')";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetNextId);
		results.next(); 
		long id = results.getLong(1);
		
		String sqlCreateReservation = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date)" +
									"VALUES (?, ?, ?, ?, ?)";
		
		jdbcTemplate.update(sqlCreateReservation, id, siteId, name, fromDate, toDate);
		
		Reservation newReservation = new Reservation();
		newReservation.setReservationId(id);
		newReservation.setSiteId(siteId);
		newReservation.setName(name);
		newReservation.setFromDate(fromDate);
		newReservation.setToDate(toDate);
		newReservation.setDateCreated(LocalDate.now());
		
		return newReservation;
	}

}
