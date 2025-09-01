package edu.ncsu.csc316.trail.comparator;

import java.util.Comparator;

import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.trail.data.Landmark;

/**
 * Compares entries in a map of Landmarks and distances. Returns a positive
 * number if entry1 has a greater distance than entry2. Returns a negative
 * number if entry1 has a shorter distance than entry2. If the distances are
 * equal, returns an integer <, >, or = 0 based on lexicographic ordering.
 * 
 * @author Thommasaht Nhouyvanisvong (tcnhouyv)
 */
public class LandmarkDistanceComparator implements Comparator<Entry<Landmark, Integer>> {

	@Override
	public int compare(Entry<Landmark, Integer> entry1, Entry<Landmark, Integer> entry2) {
		int entryOneDistance = entry1.getValue();
		int entryTwoDistance = entry2.getValue();
		
		if (entryOneDistance - entryTwoDistance == 0) {
			return entry1.getKey().getDescription().compareTo(entry2.getKey().getDescription());
		}
		return entryOneDistance - entryTwoDistance;
	}

}
