package com.techelevator.campground.model;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {
	
	public List<Site> getAvailableSites(long campgroundId, LocalDate fromDate, LocalDate toDate);
}
