package edu.ncsu.csc316.trail.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;

/**
 * Tests the TrailManger class including getDistancesToDestinations and getProposedFirstAidLocations.
 * 
 * @author Thommasaht Nhouyvanisvong (tcnhouyv)
 */
public class TrailManagerTest {

	/** TrailManager used for testing */
	private TrailManager manager;

	/**
	 * Creates an instance of TrailManager with valid input before each test.
	 */
	@Before
	public void setUp() {
		try {
			manager = new TrailManager("input/landmark_data_valid.txt", "input/trail_data_valid.txt");
		} catch (FileNotFoundException e) {
			fail("FileNotFoundException was thrown.");
		}
	}

	/**
	 * Tests the getDistancesToDestinations method including landmarks with
	 * reachable destinations, invalid landmarks, and landmarks that are
	 * unreachable.
	 */
	@Test
	public void testGetDistancesToDestinations() {
		Map<Landmark, Integer> entranceMap = manager.getDistancesToDestinations("L01");
		assertEquals(11, entranceMap.size());
		assertFalse(entranceMap.isEmpty());
		assertEquals(0, (int) entranceMap.get(manager.getLandmarkByID("L01")));
		assertEquals(200, (int) entranceMap.get(manager.getLandmarkByID("L02")));
		assertEquals(500, (int) entranceMap.get(manager.getLandmarkByID("L03")));
		assertEquals(1800, (int) entranceMap.get(manager.getLandmarkByID("L04")));
		assertEquals(2200, (int) entranceMap.get(manager.getLandmarkByID("L05")));
		assertEquals(1600, (int) entranceMap.get(manager.getLandmarkByID("L06")));
		assertEquals(6000, (int) entranceMap.get(manager.getLandmarkByID("L07")));
		assertEquals(5700, (int) entranceMap.get(manager.getLandmarkByID("L08")));
		assertEquals(6300, (int) entranceMap.get(manager.getLandmarkByID("L09")));
		assertEquals(7200, (int) entranceMap.get(manager.getLandmarkByID("L10")));
		assertEquals(7400, (int) entranceMap.get(manager.getLandmarkByID("L11")));

		Map<Landmark, Integer> campsiteMap = manager.getDistancesToDestinations("L12");
		assertEquals(3, campsiteMap.size());
		assertFalse(campsiteMap.isEmpty());
		assertEquals(0, (int) campsiteMap.get(manager.getLandmarkByID("L12")));
		assertEquals(300, (int) campsiteMap.get(manager.getLandmarkByID("L13")));
		assertEquals(300, (int) campsiteMap.get(manager.getLandmarkByID("L14")));

		Map<Landmark, Integer> pondMap = manager.getDistancesToDestinations("L15");
		assertEquals(1, pondMap.size());
		assertFalse(pondMap.isEmpty());
		assertEquals(0, (int) pondMap.get(manager.getLandmarkByID("L15")));

		Map<Landmark, Integer> invalidMap = manager.getDistancesToDestinations("L404");
		assertEquals(0, invalidMap.size());
		assertTrue(invalidMap.isEmpty());
	}

	/**
	 * Tests the getProposedFirstAidLocations method using inputs of 0-4,
	 * representing the lower and upper bounds of the data set.
	 */
	@Test
	public void testGetProposedFirstAidLocations() {
		
		Map<Landmark, List<Trail>> zeroTrails = manager.getProposedFirstAidLocations(0);
		assertEquals(0, zeroTrails.size());
		Map<Landmark, List<Trail>> negativeTrails = manager.getProposedFirstAidLocations(-1);
		assertEquals(0, negativeTrails.size());

		Map<Landmark, List<Trail>> oneIntersectingTrail = manager.getProposedFirstAidLocations(1);
		assertEquals(14, oneIntersectingTrail.size());

		Map<Landmark, List<Trail>> twoIntersectingTrails = manager.getProposedFirstAidLocations(2);
		assertEquals(8, twoIntersectingTrails.size());

		Map<Landmark, List<Trail>> threeIntersectingTrails = manager.getProposedFirstAidLocations(3);
		assertEquals(2, threeIntersectingTrails.size());

		Map<Landmark, List<Trail>> fourIntersectingTrails = manager.getProposedFirstAidLocations(4);
		assertEquals(0, fourIntersectingTrails.size());
	}
}
