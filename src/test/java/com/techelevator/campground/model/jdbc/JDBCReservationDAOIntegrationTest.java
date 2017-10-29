package com.techelevator.campground.model.jdbc;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.DAOIntegrationTest;

import org.junit.Assert;

public class JDBCReservationDAOIntegrationTest extends DAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCReservationDAO dao;
	private JdbcTemplate jdbcTemplate;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		dao = new JDBCReservationDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void makeNewReservationWorksCorrectly() {
		
		List<Long> allIds = new ArrayList<>();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		String start = "10/10/2018";
		String end = "20/10/2018";
		
		LocalDate startDate = LocalDate.parse(start, formatter);
		LocalDate endDate = LocalDate.parse(end, formatter);
		
		dao.makeNewReservation(616, "Chris", startDate, endDate);
		
		String sqlNewRes = "SELECT reservation_id " +
							"FROM reservation " +
							"WHERE site_id = 616";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlNewRes);
		
		while (results.next()) {
			long id = results.getLong("reservation_id");
			allIds.add(id);
		}
		
		Assert.assertEquals(allIds.size(), 1);
	}
}
