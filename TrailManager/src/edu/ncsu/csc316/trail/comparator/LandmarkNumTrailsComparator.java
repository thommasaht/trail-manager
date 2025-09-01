package edu.ncsu.csc316.trail.comparator;

import java.util.Comparator;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;

/**
 * Compares two entries in a map of Landmarks and Trail lists to determine
 * ordering in a list of proposed first aid stations. Returns a negative number
 * if entry1 has more intersecting trails than entry2. Returns a positive number
 * if entry2 has more intersecting trails than entry1. If both entries have the
 * same number of intersecting trails, they are compared lexicographically by
 * Landmark description.
 * 
 * @author Thommasaht Nhouyvanisvong (tcnhouyv)
 */
public class LandmarkNumTrailsComparator implements Comparator<Entry<Landmark, List<Trail>>> {

	@Override
	public int compare(Entry<Landmark, List<Trail>> entry1, Entry<Landmark, List<Trail>> entry2) {
		if (entry2.getValue().size() - entry1.getValue().size() == 0) {
			return entry1.getKey().getDescription().compareTo(entry2.getKey().getDescription());
		}
		return entry2.getValue().size() - entry1.getValue().size();
	}

}
