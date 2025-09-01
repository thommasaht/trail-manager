package edu.ncsu.csc316.trail.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.dsa.DataStructure;
import edu.ncsu.csc316.trail.manager.ReportManager;

/**
 * A class that contains the UI functionality for TrailManager, i.e. the View
 * and Controller portion of the MVC model. Allows the client to select input
 * files, navigate through TrailManager features, and close the program when
 * necessary.
 * 
 * @author Thommasaht Nhouyvanisvong (tcnhouyv)
 */
public class TrailManagerUI {

	/**
	 * Creates a console based UI that allows users to specify input files and
	 * navigate TrailManager features.
	 * 
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String landmarkFile;
		String trailFile;
		ReportManager manager = null;

		// Prompt for input files until valid landmark and trail files are found.
		do {
			System.out.print("Enter Landmark file: ");
			landmarkFile = scanner.next();
			System.out.print("Enter Trail file: ");
			trailFile = scanner.next();

			try {
				manager = new ReportManager(landmarkFile, trailFile, DataStructure.LINEARPROBINGHASHMAP);
			} catch (FileNotFoundException e) {
				System.out.println("File(s) not found.");
			}
		} while (manager == null);
		
		System.out.println("Map selected: " + DSAFactory.getMap(null).getClass().getSimpleName());
		
		// Display main menu
		String input = null;
		do {
			System.out.println("TrailManager ver 1.0");
			System.out.println("[1] List Potential Locations for First Aid Stations");
			System.out.println("[2] View Distances to All Reachable Landmarks");
			System.out.println("[3] Experiment Report for getDistancesReport(L0000001)");
			System.out.println("[Q] Quit");

			input = scanner.next();

			switch (input) {
			case "1":
				System.out.print("Enter number of intersecting trails: ");
				int numTrails = scanner.nextInt();
				System.out.println(manager.getProposedFirstAidLocations(numTrails));
				System.out.println("");
				break;
			case "2":
				System.out.print("Enter starting landmark ID: ");
				String landmarkID = scanner.next();
				System.out.println(manager.getDistancesReport(landmarkID));
				System.out.println("");
				break;
			case "3": 
				long start = System.currentTimeMillis();
				manager.getDistancesReport("L0000001");
				long stop = System.currentTimeMillis();
				long duration = stop - start;
				System.out.println("Elapsed time: " + duration);
				break;
			case "Q", "q":
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Invalid command.");
				System.out.println("");
				break;
			}
		} while (!"Q".equals(input) && !"q".equals(input));

		scanner.close();
		System.exit(0);
	}
}
