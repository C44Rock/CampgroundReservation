package com.techelevator.campground.model;

import java.time.LocalDate;

public class Park {

	private long parkId;
	private String name;
	private String location;
	private LocalDate dateEstablished;
	private long area;
	private long visitors;
	private String description;
	
	public long getParkId() {
		return parkId;
	}
	public void setParkId(long parkId) {
		this.parkId = parkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public LocalDate getDateEstablished() {
		return dateEstablished;
	}
	public void setDateEstablished(LocalDate dateEstablished) {
		this.dateEstablished = dateEstablished;
	}
	public long getArea() {
		return area;
	}
	public void setArea(long area) {
		this.area = area;
	}
	public long getVisitors() {
		return visitors;
	}
	public void setVisitors(long visitors) {
		this.visitors = visitors;
	}
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
