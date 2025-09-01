package edu.ncsu.csc316.trail.manager;

import java.io.FileNotFoundException;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.dsa.sorter.Sorter;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;
import edu.ncsu.csc316.trail.dsa.Algorithm;
import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.dsa.DataStructure;
import edu.ncsu.csc316.trail.comparator.LandmarkDistanceComparator;
import edu.ncsu.csc316.trail.comparator.LandmarkNumTrailsComparator;

/**
 * A class that sets the data structures and algorithms used for TrailManager
 * and accesses TrailManager functionality. Acts as an interface between
 * TrailManager and the UI by generating String representations of TrailManager
 * output.
 * 
 * @author Thommasaht Nhouyvanisvong (tcnhouyv)
 */
public class ReportManager {

	/** TrailManager used to handle algorithmic logic */
	private TrailManager manager;
	/** Indent used for report formatting (three spaces) */
	private static final String INDENT = "   ";
	/** Number of feet in mile used for unit conversion */
	private static final int FEET_IN_A_MILE = 5280;

	/**
	 * Creates a report manager from the specified files and sets data structures
	 * used for TrailManager methods. Allows for a custom map type to be specified
	 * as a parameter. Throws FileNotFoundException if either file is invalid.
	 * 
	 * @param pathToLandmarkFile file containing landmark information
	 * @param pathToTrailFile    file containing trail information
	 * @param mapType            custom map to be used
	 * @throws FileNotFoundException if either parameter is invalid
	 */
	public ReportManager(String pathToLandmarkFile, String pathToTrailFile, DataStructure mapType)
			throws FileNotFoundException {
		DSAFactory.setMapType(mapType);
		DSAFactory.setListType(DataStructure.SINGLYLINKEDLIST);
		DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
		DSAFactory.setNonComparisonSorterType(Algorithm.RADIX_SORT);

		manager = new TrailManager(pathToLandmarkFile, pathToTrailFile, mapType);
	}

	/**
	 * Creates a report manager from the specified files and sets data structures
	 * used for TrailManager methods. Throws FileNotFoundException if either file is
	 * invalid.
	 * 
	 * @param pathToLandmarkFile file containing landmark information
	 * @param pathToTrailFile    file containing trail information
	 * @throws FileNotFoundException if either parameter is invalid
	 */
	public ReportManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {
		this(pathToLandmarkFile, pathToTrailFile, DataStructure.SKIPLIST);
	}

	/**
	 * Returns a string report containing landmarks reachable from originLandmark,
	 * their distances from originLandmark, and their landmark ID.
	 * 
	 * @param originLandmark landmark to find reachable landmarks from.
	 * @return a string containing landmarks, distances, and IDs of landmarks
	 *         reachable from originLandmark
	 */
	public String getDistancesReport(String originLandmark) {
		Map<Landmark, Integer> distances = manager.getDistancesToDestinations(originLandmark);
		Landmark origin = manager.getLandmarkByID(originLandmark);
		if (distances.size() == 0) {
			return "The provided landmark ID (" + originLandmark + ") is invalid for the park.";
		}
		if (distances.size() == 1) {
			return "No landmarks are reachable from " + origin.getDescription() + " (" + originLandmark + ").";
		}
		// Add distancesList entries to an array.
		@SuppressWarnings("unchecked")
		Entry<Landmark, Integer>[] entryArr = new Entry[distances.size()];
		int i = 0;
		for (Entry<Landmark, Integer> entry : distances.entrySet()) {
			entryArr[i] = entry;
			i++;
		}
		// Sort entryArr by ascending distance
		Sorter<Entry<Landmark, Integer>> sorter = DSAFactory.getComparisonSorter(new LandmarkDistanceComparator());
		sorter.sort(entryArr);

		// Generate String containing landmark distances
		StringBuilder sb = new StringBuilder();
		String originDesc = origin.getDescription();
		String originID = origin.getId();
		sb.append("Landmarks Reachable from ").append(originDesc).append(" (").append(originID).append(") {\n");
		for (int j = 1; j < entryArr.length; j++) {
			int distanceInFeet = entryArr[j].getValue();
			String landmarkDesc = entryArr[j].getKey().getDescription();
			String landmarkID = entryArr[j].getKey().getId();

			sb.append(INDENT).append(distanceInFeet).append(" feet ");
			if (entryArr[j].getValue() > FEET_IN_A_MILE) {
				double distanceInMiles = (double) distanceInFeet / FEET_IN_A_MILE;
				sb.append("(").append(String.format("%.2f", distanceInMiles)).append(" miles) ");
			}
			sb.append("to ").append(landmarkDesc).append(" (").append(landmarkID).append(")\n");
		}
		sb.append("}");

		return sb.toString();
	}

	/**
	 * Returns a string report of landmarks containing >= numberOfIntersectingTrails
	 * trails that intersecting with the landmarks, their ID, and number of
	 * intersecting trails.
	 * 
	 * @param numberOfIntersectingTrails minimum number of trails to intersect with
	 *                                   landmarks.
	 * @return a string containing landmarks with at least
	 *         numberOfIntersectingTrails intersecting trails.
	 */
	public String getProposedFirstAidLocations(int numberOfIntersectingTrails) {
		Map<Landmark, List<Trail>> firstAidLocations = manager.getProposedFirstAidLocations(numberOfIntersectingTrails);
		if (numberOfIntersectingTrails <= 0) {
			return "Number of intersecting trails must be greater than 0.";
		}
		if (firstAidLocations.size() == 0) {
			return "No landmarks have at least " + numberOfIntersectingTrails + " intersecting trails.";
		}
		// Add firstAidLocation entries to an array
		@SuppressWarnings("unchecked")
		Entry<Landmark, List<Trail>>[] entryArr = new Entry[firstAidLocations.size()];
		int i = 0;
		for (Entry<Landmark, List<Trail>> entry : firstAidLocations.entrySet()) {
			entryArr[i] = entry;
			i++;
		}
		// Sort firstAidLocation entries by num of intersecting trails/alphabetically
		Sorter<Entry<Landmark, List<Trail>>> sorter = DSAFactory.getComparisonSorter(new LandmarkNumTrailsComparator());
		sorter.sort(entryArr);
		// Output formatted String
		StringBuilder sb = new StringBuilder();
		sb.append("Proposed Locations for First Aid Stations {\n");
		for (Entry<Landmark, List<Trail>> entry : entryArr) {
			String landmarkDesc = entry.getKey().getDescription();
			String landmarkID = entry.getKey().getId();
			int currentIntersectingTrails = entry.getValue().size();

			sb.append(INDENT).append(landmarkDesc).append(" (").append(landmarkID).append(") - ")
					.append(currentIntersectingTrails).append(" intersecting trails\n");
		}
		sb.append("}");

		return sb.toString();
	}

}
