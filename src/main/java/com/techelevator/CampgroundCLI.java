package com.techelevator;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.model.jdbc.JDBCReservationDAO;
import com.techelevator.campground.model.jdbc.JDBCSiteDAO;

public class CampgroundCLI {
	
	private static final String[] PARK_MENU_OPTIONS = { "View Campgrounds", "Search for Reservation", "Return to Previous Screen" };
	private static final String[] RESERVATION_MENU_OPTIONS = { "Search for Available Reservation", "Return to Main Menu" };
	
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;
	private Menu menu;
	private long requestedId;
	private double dailyFee;
	List<Site> availableSites;
	private long closeDate;
	private long openDate;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		// create your DAOs here
		
		parkDAO = new JDBCParkDAO(datasource);
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
		reservationDAO = new JDBCReservationDAO(datasource);
		this.menu = new Menu(System.in, System.out);
	}
	
	public void run() {
		while (true) {
			printHeading("View Parks Interface");
			List<Park> allParks = parkDAO.getAllParks();
			
			Park selectedPark = (Park)menu.getChoiceFromOptions(allParks.toArray());
			
			System.out.println();
			printHeading("Park Information Screen");
			System.out.println();
			System.out.println(selectedPark.getName() + " National Park");
			System.out.println();
			System.out.println(String.format("%-5s%-16s", "Location: ", selectedPark.getLocation()));
			System.out.println(String.format("%-5s%-16s", "Established: ", selectedPark.getDateEstablished()));
			System.out.println(String.format("%-5s%,d sq km", "Area: ", selectedPark.getArea()));
			System.out.println(String.format("%-5s%,d", "Annual Visitors: ", selectedPark.getVisitors()));
			System.out.println();
			System.out.println(selectedPark.getDescription());
			
			////while loop??
			while (true) {
				String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
				if(choice.equals(PARK_MENU_OPTIONS[0])) {
					while(true) {
						System.out.println();
						printHeading("Park Campgrounds");
						this.displayCampgrounds(selectedPark);
			
						String reservationChoice = (String)menu.getChoiceFromOptions(RESERVATION_MENU_OPTIONS);
						if (reservationChoice.equals(RESERVATION_MENU_OPTIONS[0])) {
							this.searchForReservation(selectedPark);
							//this.getReservationDetails();
							break;
						} if (reservationChoice.equals(RESERVATION_MENU_OPTIONS[1])) {
							break;
						}
					}
				} if (choice.equals(PARK_MENU_OPTIONS[1])) {
					while (true) {
						System.out.println();
						printHeading("Search for Campground Reservation");
						this.searchForReservation(selectedPark);
					//	this.getReservationDetails();
						break;
					}
				} if (choice.equals(PARK_MENU_OPTIONS[2])) {
					break; // LOOP BACK??
				}
			}
		}
	}
	
	
	
	
	
	private void displayCampgrounds(Park park) {
		System.out.println();
		System.out.println(String.format("%-5s%-35s%-10s%-10s%-10s", "#", "Name", "Open", "Close", "Daily Fee"));
		
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgroundsByPark(park.getParkId());
		for (Campground cg : allCampgrounds) {
			System.out.println((String.format("%-5s%-35s%-10s%-10s%-10.2f", cg.getCampgroundId(), cg.getName(), cg.getOpenDate(), cg.getCloseDate(), cg.getDailyFee())));
		}
	}
	
	
	
	
	private void searchForReservation(Park park) {
		this.displayCampgrounds(park);
		System.out.println();
		
		List<Campground> campgrounds = campgroundDAO.getAllCampgroundsByPark(park.getParkId());
		List<Long> campIds = new ArrayList<>();
		
		Scanner cgScanner = new Scanner(System.in);
		boolean correct = false;
		
		while (!correct) {
			System.out.println("Which campground # would you like? (enter 0 to cancel):");
			requestedId = cgScanner.nextLong();
				
			for (Campground campground : campgrounds) {
				if (campground.getCampgroundId() == requestedId) {
					dailyFee = campground.getDailyFee();
					closeDate = Integer.parseInt(campground.getCloseDate());
					openDate = Integer.parseInt(campground.getOpenDate());
				}
				long thisId = campground.getCampgroundId();
				campIds.add(thisId);
			}
			if (campIds.contains(requestedId)) {
				correct = true;
				System.out.println();
			} else if (requestedId == 0) {
				return; // LOOP BACK?
			} else {
				System.out.println("Please select a valid campground");
				System.out.println();
			}
		}
		
		correct = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		LocalDate arrivalDate = null;
		LocalDate departureDate = null;
		
		while (!correct) {
			System.out.println("What is your preferred arrival date? (dd/MM/yy): ");
			String arrivalString = cgScanner.next();
			
			
			try {
				arrivalDate = LocalDate.parse(arrivalString, formatter);
				if (arrivalDate.isBefore(LocalDate.now())) throw new Exception();
				if (arrivalDate.getMonthValue() <= openDate) throw new Exception();
				System.out.println();
				break;
			} catch (Exception e) {
				System.out.println();
				System.out.println("Please check the date's format and validity, as well as campground open months.");
				System.out.println();
			}
		}
		
		while (!correct) {
			System.out.println("What is your preferred departure date? (dd/MM/yy): ");
			String departureString = cgScanner.next();
			try {
				departureDate = LocalDate.parse(departureString, formatter);
				if (departureDate.isBefore(LocalDate.now()) || arrivalDate.isAfter(departureDate)) throw new Exception();
				if (departureDate.getMonthValue() > closeDate) throw new Exception();
				System.out.println();
				break;
			} catch (Exception e) {
				System.out.println();
				System.out.println("Please check the date's format and validity, as well as campground open months.");
				System.out.println();
			}
		}
			this.displayCampgroundSearchResults(arrivalDate, departureDate);
			getReservationDetails(arrivalDate, departureDate);
	}
	
	
	
	
	private void displayCampgroundSearchResults(LocalDate arrivalDate, LocalDate departureDate) {
		printHeading("Results Matching Your Criteria");
		
		Period period = Period.between(arrivalDate, departureDate);
		Integer daysElapsed = period.getDays();
		
		double costOfTheStay = daysElapsed * dailyFee;
		
		availableSites = siteDAO.getAvailableSites(requestedId, arrivalDate, departureDate);
		
		System.out.println(String.format("%-10s%-15s%-15s%-15s%-15s%-15s", "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utilities?", "Cost"));
		
		for (Site site : availableSites) {
			System.out.println(String.format("%-10s%-15s%-15s%-15s%-15s%-15.2f", site.getSiteId(), site.getMaxOccupancy(), site.isAccessible(), site.getMaxLengthRV(), site.isUtilities(), costOfTheStay));
		}
	}
	
	
	
	
	
	private void getReservationDetails(LocalDate arrivalDate, LocalDate departureDate) {
		
		Scanner rScanner = new Scanner(System.in);
		List<Long> siteIds = new ArrayList<>();
		long reserveSiteId = 0;
		
		boolean correct = false;
		
		for (Site site : availableSites) {
			siteIds.add(site.getSiteId());
		}
		
		while (!correct) {
			System.out.println();
			System.out.println("Which site # would you like to reserve? (enter 0 to cancel)");
			reserveSiteId = rScanner.nextLong();
			rScanner.nextLine();
			
			if (reserveSiteId == 0) {
				System.exit(0);
			} else if (!siteIds.contains(reserveSiteId)) {
				System.out.println("Please enter a valid site #");
			} else {
				correct = true;
				System.out.println();
			}
		}
		
		System.out.println("What name should the reservation be made under?");
		String reserveName = rScanner.nextLine();
		
		Reservation reservation = reservationDAO.makeNewReservation(reserveSiteId, reserveName, arrivalDate, departureDate);
		
		System.out.println();
		System.out.println("The reservation has been made and the confirmation id is: " + reservation.getReservationId());
	}
	
	
	
	
	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
	
}
