package com.techelevator.campground.model.jdbc;

import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.DAOIntegrationTest;
import com.techelevator.campground.model.Campground;
import org.junit.Assert;

public class JDBCCampgroundDAOIntegrationTest extends DAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO dao;
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
		dao = new JDBCCampgroundDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void getAllCampgroundsByParkWorksCorrectly() {
		
		String sqlUpdateCampground = "INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) " +
				   					"VALUES (100, 3, 'Mt. Everest', 1, 12, 20)";

		jdbcTemplate.update(sqlUpdateCampground);
		
		List<Campground> cgForPark3 = dao.getAllCampgroundsByPark(3);
		
		Assert.assertEquals(cgForPark3.size(), 2);
	}
}
