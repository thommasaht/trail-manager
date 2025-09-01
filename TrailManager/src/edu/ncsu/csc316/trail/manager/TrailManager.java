package edu.ncsu.csc316.trail.manager;

import java.io.FileNotFoundException;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.queue.Queue;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;
import edu.ncsu.csc316.trail.dsa.Algorithm;
import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.dsa.DataStructure;
import edu.ncsu.csc316.trail.io.TrailInputReader;

/**
 * A class that maintains trail and landmark information within the TrailManager
 * system. TrailManager contains functionality for getting distances to all
 * landmarks from an origin landmark, searching landmarks by ID, and getting a
 * list of proposed first aid locations by searching for landmarks with an
 * intersecting trail threshold.
 * 
 * @author Thommasaht Nhouyvanisvong (tcnhouyv)
 */
public class TrailManager {

	/** Map of landmark IDs and Landmarks */
	private Map<String, Landmark> landmarkIDMap;
	/** Map of Landmarks and intersecting Trails */
	private Map<Landmark, List<Trail>> landmarkTrailMap;

	/**
	 * Creates a TrailManager from input files containing landmarks and trails.
	 * Allows for custom selection of a map type.
	 * 
	 * @param pathToLandmarkFile name of landmark file
	 * @param pathToTrailFile    name of trail file
	 * @param mapType            type of map to be used
	 * @throws FileNotFoundException if either file is invalid
	 */
	public TrailManager(String pathToLandmarkFile, String pathToTrailFile, DataStructure mapType)
			throws FileNotFoundException {
		DSAFactory.setMapType(mapType);
		DSAFactory.setListType(DataStructure.SINGLYLINKEDLIST);
		DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
		DSAFactory.setNonComparisonSorterType(Algorithm.RADIX_SORT);

		addLandmarksToMap(TrailInputReader.readLandmarks(pathToLandmarkFile));
		addTrailsToMap(TrailInputReader.readLandmarks(pathToLandmarkFile),
				TrailInputReader.readTrails(pathToTrailFile));
	}

	/**
	 * Creates a TrailManager from an input file containing landmarks and an input
	 * file containing trails. Throws a FileNotFoundException if either file is
	 * invalid.
	 * 
	 * @param pathToLandmarkFile name of landmark file
	 * @param pathToTrailFile    name of trail file
	 * @throws FileNotFoundException if either file is invalid
	 */
	public TrailManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {
		this(pathToLandmarkFile, pathToTrailFile, DataStructure.SKIPLIST);
	}

	/**
	 * This method traverses all trails that intersect with originLandmark, adding
	 * landmarks not currently in the map to return a map containing all landmarks
	 * reachable from originLandmark and their distances from originLandmark.
	 * 
	 * @param originLandmark landmark to determine reachable landmarks
	 * @return map containing reachable landmarks and their distances
	 */
	public Map<Landmark, Integer> getDistancesToDestinations(String originLandmark) {
		Map<Landmark, Integer> distancesMap = DSAFactory.getMap(null);
		Landmark origin = getLandmarkByID(originLandmark);
		// Error handling. If originLandmark is not in the system, an empty map is
		// returned.
		if (origin == null) {
			return distancesMap;
		}

		Queue<Landmark> visitedLandmarks = DSAFactory.getQueue();

		// place origin landmark in distancesMap with distance of 0
		distancesMap.put(origin, 0);
		List<Trail> currentTrailList = landmarkTrailMap.get(origin);
		// For each trail intersecting with the origin, we add the opposite landmark and
		// its distance.
		// We also add each visited landmark to the visitedLandmarks for later
		// traversal.
		for (Trail t : currentTrailList) {
			Landmark landmarkOne = getLandmarkByID(t.getLandmarkOne());
			if (!landmarkOne.equals(origin)) {
				distancesMap.put(landmarkOne, t.getLength());
				visitedLandmarks.enqueue(landmarkOne);
			} else {
				Landmark landmarkTwo = getLandmarkByID(t.getLandmarkTwo());
				distancesMap.put(landmarkTwo, t.getLength());
				visitedLandmarks.enqueue(landmarkTwo);
			}
		}
		// For each visited landmark, we visit all trails in its trail list and add
		// landmarks not currently in distancesMap, also enqueueing new landmarks in
		// visitedLandmarks.
		while (!visitedLandmarks.isEmpty()) {
			Landmark currentLandmark = visitedLandmarks.dequeue();
			currentTrailList = landmarkTrailMap.get(currentLandmark);
			int currentDistance = distancesMap.get(currentLandmark);
			for (Trail t : currentTrailList) {
				Landmark landmarkOne = getLandmarkByID(t.getLandmarkOne());
				if (!currentLandmark.equals(landmarkOne)) {
					// If the opposite landmark is not in distancesMap, we put it in with its length
					// + the distance of the current landmark from the origin as the value.
					if (distancesMap.get(landmarkOne) == null) {
						distancesMap.put(landmarkOne, t.getLength() + currentDistance);
						visitedLandmarks.enqueue(landmarkOne);
					}
				} else {
					Landmark landmarkTwo = getLandmarkByID(t.getLandmarkTwo());
					if (distancesMap.get(landmarkTwo) == null) {
						distancesMap.put(landmarkTwo, t.getLength() + currentDistance);
						visitedLandmarks.enqueue(landmarkTwo);
					}
				}
			}
		}

		return distancesMap;
	}

	/**
	 * Private helper method for adding landmarks in a list to landmarkIDMap.
	 * 
	 * @param list list of landmarks added to the system
	 */
	private void addLandmarksToMap(List<Landmark> list) {
		landmarkIDMap = DSAFactory.getMap(null);
		for (Landmark l : list) {
			landmarkIDMap.put(l.getId(), l);
		}
	}

	/**
	 * Private helper method for adding a list to trails to each landmark in
	 * landmarkTrailMap.
	 * 
	 * @param landmarkList list containing landmarks on system
	 * @param trailList    list containing trails to add
	 */
	private void addTrailsToMap(List<Landmark> landmarkList, List<Trail> trailList) {
		landmarkTrailMap = DSAFactory.getMap(null);
		for (Landmark l : landmarkList) {
			landmarkTrailMap.put(l, DSAFactory.getIndexedList());
		}
		for (Trail t : trailList) {
			landmarkTrailMap.get(getLandmarkByID(t.getLandmarkOne())).addLast(t);
			landmarkTrailMap.get(getLandmarkByID(t.getLandmarkTwo())).addLast(t);
		}
	}

	/**
	 * Returns the Landmark corresponding with the specified landmark ID.
	 * 
	 * @param landmarkID landmark ID to search
	 * @return landmark containing landmarkID
	 */
	public Landmark getLandmarkByID(String landmarkID) {
		return landmarkIDMap.get(landmarkID);
	}

	/**
	 * Returns a map of landmarks and list of trails with a number of intersecting
	 * trails >= the parameter. If no landmarks contain the specified number of
	 * trails, an empty map is returned.
	 * 
	 * @param numberOfIntersectingTrails number of intersecting trails a landmark
	 *                                   must contain
	 * @return map containing Landmarks with at least numberOfIntersectingTrails
	 */
	public Map<Landmark, List<Trail>> getProposedFirstAidLocations(int numberOfIntersectingTrails) {
		Map<Landmark, List<Trail>> firstAidLocations = DSAFactory.getMap(null);
		// Return an empty map if numberOfIntersectingTrails is <= 0
		if (numberOfIntersectingTrails <= 0) {
			return firstAidLocations;
		}
		// Add landmarks with a trailList whose size is >= numberOfIntersectingTrails
		for (Landmark landmark : landmarkTrailMap) {
			List<Trail> currentTrailList = landmarkTrailMap.get(landmark);
			if (currentTrailList.size() >= numberOfIntersectingTrails) {
				firstAidLocations.put(landmark, currentTrailList);
			}
		}
		return firstAidLocations;
	}
}
