package project5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class handles the actual program, including opening/reading the CSV file,
 * validating the name objects, and handling user input
 * @author sarahwardles
 *
 */
public class NYSBabyNames {

	/**
	 * Main method that opens and reads data 
	 * @author sarahwardles
	 * @param args
	 *   specifies one argument to the program (the file)
	 */

	public static void main(String[] args) {
		//VALIDATING THE FILE PORTION
		//validate the number or command line arguments to open file
		if (args.length == 0 ) {
			System.err.println("Usage Error: the program expects file name as an argument.");
			System.exit(1);
		}
		
		File file = new File(args[0]);
		//check if the file specified by the args can be opened/exists
		if (!file.exists()) {
			System.err.println("Error: the file at " + file.getAbsolutePath() + " does not exist.");
			System.exit(1);
		}
		
		// Check that the file specified by the args can be read
		if (!file.canRead()) {
	    	System.err.printf("ERROR: cannnot read the file %s.", args[0]);
			System.exit(1);
		}
		
		//create new scanner
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(file);
		} 
		//if not throw an error
		catch (FileNotFoundException e) {
			System.err.println("Error: there is a problem with the file at " + file.getAbsolutePath());
			System.exit(1);
		}
		
		ArrayList<YearNames> allYears = new ArrayList<YearNames>();
		
		//variables to hold data for csv file and interaction
		String entry = null;
		Scanner readEntry = null;
		String name = null;
		String gender = null;
		int count = 0;
		int year = 0;
		String county = null;
		Name stored = null;
		boolean exists = false;
		
		//CSV FILE PARSING PORTION
		//while we are not at the end of the file
		//go though each line in the file and validate that is has 5 elements	
		while (scanner.hasNextLine()) {
			try {
				entry = scanner.nextLine();
				readEntry = new Scanner(entry);
				readEntry.useDelimiter(",");

				//skip the header
				//keep all the data for each line in file
				try {
					year = Integer.parseInt(readEntry.next());
				} 
				catch (NumberFormatException e) {
					continue;
				}
				name = readEntry.next();
				county = readEntry.next();
				gender = readEntry.next();

				try {
					count = Integer.parseInt(readEntry.next());
				} 
				catch (NumberFormatException e) {
					continue;
				}
			} catch (NoSuchElementException e) {
				System.err.println("Error: "+ entry);
			}
			
			//try to make a name object with the feilds of the file
			try {
				stored = new Name(name, gender, count, county);
				
			} catch (IllegalArgumentException e) {
				continue; // if invalid parameters just continue to next line in file
			}
			
			exists = false;
			
			//iteracte over all the years
			for (int i = 0; i < allYears.size(); i++) {
				if (allYears.get(i).getYear() == year) {
					allYears.get(i).add(stored);
					exists = true;
				}
			}
			
			//if the year is not found in the arraylist then create a new yearnames obj with it
			if (!exists) {
				allYears.add(new YearNames(year));
				allYears.get(allYears.size()-1).add(stored);
			}
		}

		//USER INERACTIVE PORTION
		//useful varaibles for interaction
		Scanner userin = new Scanner(System.in);
		String inputName= "";
		String inputCounty = "";
		boolean ispresent = false;
		double fraction = 0;
		
		while (!(inputName.equalsIgnoreCase("q"))) {
			ispresent = false;
			System.out.println("");
			System.out.print("Enter a name [q to exit]: ");
			inputName= userin.nextLine();
			//check for keyword q(quit)
			if (inputName.equalsIgnoreCase("q")) {
				break;
			}
			
			System.out.println("");
			System.out.print("Please enter a county (ALL, for search in all counties): ");
			inputCounty = userin.nextLine();
			
			//check for all keyword
			//check to find requested name/county
			if (inputCounty.equalsIgnoreCase("all")) {
				for ( YearNames yeartest : allYears ) {
					fraction = yeartest.getFractionByName(inputName);
					if (fraction > 0) {
						ispresent = true;
						break;
					}
				}
			} else {
				for ( YearNames yeartest : allYears ) {
					fraction = yeartest.getFractionByNameCounty(inputName, inputCounty);
					if (fraction > 0) {
						ispresent = true;
						break;
					}
				}
			}
			
			//if actually found the input
			if (ispresent) {
				if (inputCounty.equalsIgnoreCase("all")) {
					System.out.println("\n");
					for ( YearNames yeartest : allYears ) {
						//print out histogram for all
						histogramForName(yeartest, inputName);
					}
				} else {
					System.out.println("\n");
					for ( YearNames yeartest : allYears ) {
						//print out histogram for one county
						histogramForCounty(yeartest, inputName, inputCounty);
					}
				}
			} else {
				//if havent found inputted name/county pair
				System.out.println("No such name/county in the dataset.");
			}
			
		}
		System.out.println("");
		userin.close();
	}
	
	/**
	 * This method finds the fraction of occurences of a specified name in a year and prints
	 * out the apropriate histogram for that name for that year.
	 * @param year (year to find histogram for)
	 * @param name (the name to find)
	 */
	public static void histogramForName(YearNames year, String name) {
		//get the fraction for the name in oen year
		double fract = year.getFractionByName(name);
		String HISTOGRAM = "";

		//for every .01
		for (int i = 0; i < (fract*10000); i += 1) {
			HISTOGRAM += "|";
		}
		//formatting the print
		System.out.printf(year.getYear() + " (%.4f): "+ HISTOGRAM + "\n", (fract*100));
		
	}

	/**
	 * This method finds the fraction of occurences of a specified name in specifed county in a year and prints
	 * out the apropriate histogram for that name for that year.
	 * @param year (year to find histogram for)
	 * @param name (the name to find)
	 */
	public static void histogramForCounty(YearNames year, String name, String county) {
		double fract = year.getFractionByNameCounty(name, county);
		String HISTOGRAM = "";
		for (int i = 0; i < (fract*10000); i += 1) {
			HISTOGRAM += "|";
		}
		System.out.printf(year.getYear() + " (%.4f): "+ HISTOGRAM + "\n", (fract*100));
	}
}