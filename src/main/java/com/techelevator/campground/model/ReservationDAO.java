package com.techelevator.campground.model;

import java.time.LocalDate;

public interface ReservationDAO {
	
	public Reservation makeNewReservation(long siteId, String name, LocalDate fromDate, LocalDate toDate);
}
