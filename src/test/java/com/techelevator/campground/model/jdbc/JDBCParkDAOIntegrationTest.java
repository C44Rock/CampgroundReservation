package com.techelevator.campground.model.jdbc;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.DAOIntegrationTest;
import com.techelevator.campground.model.Park;

public class JDBCParkDAOIntegrationTest extends DAOIntegrationTest{
	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO dao;
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
		dao = new JDBCParkDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void getAllParksWorksCorrectly() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		String date = "01/01/1900";
		LocalDate newDate = LocalDate.parse(date, formatter);
		
		String addPark = "INSERT INTO park (park_id, name, location, establish_date, area, visitors, description) " +
							"VALUES (4, 'Highbanks', 'Ohio', ?, 1000, 23423, 'Hello')";
		
		jdbcTemplate.update(addPark, newDate);
		
		List<Park> allParks = dao.getAllParks();
		
		boolean found = false;
		for (Park park : allParks) {
			if (park.getParkId() == 4) {
				found = true;
			}
		}
		
		Assert.assertTrue(allParks.size() >= 1);
		Assert.assertTrue("The park should be in the list", found);
	}
	
	@Test
	public void getParkByIdWorksCorrectly() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		String date = "01/01/1900";
		LocalDate newDate = LocalDate.parse(date, formatter);
		
		String addPark = "INSERT INTO park (park_id, name, location, establish_date, area, visitors, description) " +
							"VALUES (4, 'Highbanks', 'Ohio', ?, 1000, 23423, 'Hello')";
		
		jdbcTemplate.update(addPark, newDate);
		
		Park thePark = dao.getParkById(4);
		
		Assert.assertEquals(thePark.getName(), "Highbanks");
	}
}
