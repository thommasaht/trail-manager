package edu.ncsu.csc316.trail.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the ReportManager class by generating the distances report and proposed
 * first aid locations and checking expected output.
 * 
 * @author Thommasaht Nhouyvanisvong (tcnhouyv)
 */
public class ReportManagerTest {

	/** ReportManager used for testing */
	private ReportManager manager;

	/**
	 * Constructs a new ReportManager for testing and test that it can be created
	 * with valid input.
	 */
	@Before
	public void setUp() {
		try {
			manager = new ReportManager("input/landmark_data_valid.txt", "input/trail_data_valid.txt");
		} catch (FileNotFoundException e) {
			fail("FileNotFoundException was thrown.");
		}
	}

	/**
	 * Tests the getDistancesReport method with reachable destinations, invalid
	 * landmarks, and unreachable landmarks and the expected string output.
	 */
	@Test
	public void testGetDistancesReport() {
		assertEquals("Landmarks Reachable from Park Entrance (L01) {"
				+ "\n   200 feet to Restroom 1 (L02)"
				+ "\n   500 feet to Visitor Center (L03)"
				+ "\n   1600 feet to Open Meadow (L06)"
				+ "\n   1800 feet to Waste Station 1 (L04)"
				+ "\n   2200 feet to Waste Station 2 (L05)"
				+ "\n   5700 feet (1.08 miles) to Lake (L08)"
				+ "\n   6000 feet (1.14 miles) to Waterfall (L07)"
				+ "\n   6300 feet (1.19 miles) to Rock Formation 1 (L09)"
				+ "\n   7200 feet (1.36 miles) to Overlook 1 (L10)"
				+ "\n   7400 feet (1.40 miles) to Restroom 2 (L11)"
				+ "\n}", manager.getDistancesReport("L01"));
		
		assertEquals("Landmarks Reachable from Campsite 1 (L12) {"
				+ "\n   300 feet to Overlook 2 (L14)"
				+ "\n   300 feet to Showers 1 (L13)"
				+ "\n}", manager.getDistancesReport("L12"));
		
		assertEquals("The provided landmark ID (L404) is invalid for the park.", manager.getDistancesReport("L404"));
		
		assertEquals("No landmarks are reachable from Pond (L15).", manager.getDistancesReport("L15"));
	}

	/**
	 * Tests the getProposedFirstAidLocations method with inputs of 0-4,
	 * representing the lower to upper bounds of the data set and the expected
	 * string output.
	 */
	@Test
	public void testGetProposedFirstAidLocations() {
		assertEquals("Number of intersecting trails must be greater than 0.", manager.getProposedFirstAidLocations(0));
		
		assertEquals("Proposed Locations for First Aid Stations {"
				+ "\n   Park Entrance (L01) - 3 intersecting trails"
				+ "\n   Waterfall (L07) - 3 intersecting trails"
				+ "\n   Campsite 1 (L12) - 2 intersecting trails"
				+ "\n   Overlook 1 (L10) - 2 intersecting trails"
				+ "\n   Restroom 1 (L02) - 2 intersecting trails"
				+ "\n   Visitor Center (L03) - 2 intersecting trails"
				+ "\n   Waste Station 1 (L04) - 2 intersecting trails"
				+ "\n   Waste Station 2 (L05) - 2 intersecting trails"
				+ "\n   Lake (L08) - 1 intersecting trails"
				+ "\n   Open Meadow (L06) - 1 intersecting trails"
				+ "\n   Overlook 2 (L14) - 1 intersecting trails"
				+ "\n   Restroom 2 (L11) - 1 intersecting trails"
				+ "\n   Rock Formation 1 (L09) - 1 intersecting trails"
				+ "\n   Showers 1 (L13) - 1 intersecting trails"
				+ "\n}", manager.getProposedFirstAidLocations(1));
		
		assertEquals("Proposed Locations for First Aid Stations {"
				+ "\n   Park Entrance (L01) - 3 intersecting trails"
				+ "\n   Waterfall (L07) - 3 intersecting trails"
				+ "\n   Campsite 1 (L12) - 2 intersecting trails"
				+ "\n   Overlook 1 (L10) - 2 intersecting trails"
				+ "\n   Restroom 1 (L02) - 2 intersecting trails"
				+ "\n   Visitor Center (L03) - 2 intersecting trails"
				+ "\n   Waste Station 1 (L04) - 2 intersecting trails"
				+ "\n   Waste Station 2 (L05) - 2 intersecting trails"
				+ "\n}", manager.getProposedFirstAidLocations(2));
		
		assertEquals("Proposed Locations for First Aid Stations {"
				+ "\n   Park Entrance (L01) - 3 intersecting trails"
				+ "\n   Waterfall (L07) - 3 intersecting trails"
				+ "\n}", manager.getProposedFirstAidLocations(3));
		
		assertEquals("No landmarks have at least 4 intersecting trails.", manager.getProposedFirstAidLocations(4));
	}

}
