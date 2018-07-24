package project5;
import java.util.ArrayList;

/**
 * This class is an AVL Tree implementation of a class that holds and stores Name objects.
 * Each YearName object has a specific year associated with it and holds Name objects inside
 * its own AVL Tree. Each YearName objects is effectivley an AVL Tree.
 * This class extends from AVLTree<Name>
 * @author Sarah Wardles
 *
 */
public class YearNames extends AVLTree<Name> {

	//year
	protected int year = 0;
	//variable to hold the total number of births for one year
	protected int totalBirths = 0;
	
	/**
	 * This is the constructor for the YearNames object. There is no default constructor.
	 * This constructor must be passed with an integer specifing the year for which the 
	 * YearName object is being instantiated.
	 * @param year (must be a positive integer that is between 1900 and 2018)
	 * @throws IllegalArgumentException (thrown if the constructor is passed with an invalid year)
	 */
	public YearNames(int year) {
		//check if the year is valid
		if (year < 1900 || year > 2018 ) {
			throw new IllegalArgumentException("Then program must take a valid year as a parameter.");
		}
		
		this.year = year;
	}

	/**
	 * Helper method that returns the year for a specific YearNames object.
	 * @return this.year
	 */
	public int getYear() {
		return this.year;
	}
	
	/**
	 * Method that adds Name objects to a specific instance of YearNames. Keeps track of the
	 * number of babies born in that year.
	 * @param name (Name object to be added to the list)
	 */
	public void add(Name name) {
		super.add(name);
		this.totalBirths += name.getCount();
	}
	
	//traverse the tree
	//keep count of all nodes with name (name)
	//return count

	/**
	 * Method that returns the number of babies with the specified name. Included babies 
	 * with the specified name from both genders (f/m).
	 * @param name (name to be queried)
	 * @return count (number of babies found with that name in year)
	 */
	public int getCountByName (String name) {
		int count = 0;
		
		count += getCountByName(name, this.root);
		
		return count;
	}
	/**
	* Actual recursice implementation of the getCountByName() method.
	* @param name (name to find) and node (the node to begin recursion from)
	* @return count (number of babies found)
	*/
	protected int getCountByName (String name, Node<Name> node) {
		if (node == null) {
			return 0; // if you've reached a null reference, end recursion
		}
		int count = 0;
		//if the name is less than node.name traverse left
		if (name.compareToIgnoreCase(node.getData().getName()) < 0) {
			count += getCountByName(name, node.left);
		}
		//if the name is more than the node.name traverse right
		if (name.compareToIgnoreCase(node.getData().getName()) > 0) {
			count += getCountByName(name, node.right);
		}
		//if the names are equal
		if (name.equalsIgnoreCase(node.getData().getName())) {
			count += node.getData().getCount() + getCountByName(name, node.left) + getCountByName(name, node.right);
		}
		return count;
	}
	
	/**
	 * Method that returns the fraction of babies that were given the name specified by the
	 * argument (number of babies with the name/ total number of abbies born in that year)
	 * @param name (name to be found)
	 * @return fraction 
	 */
	public double getFractionByName (String name) {
		//if the tree is empty return 0
		if (totalBirths == 0) { 
			return 0;
		}
		double fract = 0;
		
		fract = (double) getCountByName(name); //cast for safe returning/avoiding errors
		fract /= totalBirths; 
		
		return fract;
	}
	
	/**
	* Method that returns the number of babies with name specified as an argument in the
	* specified county as a parameter. Includes babies with both genders (f/m).
	* @param name (the name to be found) and county (the county to be found)
	* @return count (number of babies)
	*/
	public int getCountByNameCounty (String name, String county) {
		int count = 0;
		
		count = getCountByNameCounty(name, county, this.root, count);
		
		return count;
	}
	
	/**
	* Actual recursive inplementation of the getCountByNameCounty method
	*@param name (name to be found) and county (county to be found) and node (node to start recursion from) and count (count to return)
	*@return count (number of babies found)
	*/
	protected int getCountByNameCounty(String name, String county, Node<Name> node, int count) {
		//if reach a leaf
		if (node == null) {
			return 0; 
		}
		//if the name is less than node.name traverse left
		if (name.compareToIgnoreCase(node.getData().getName()) < 0) {
			count += getCountByNameCounty(name, county, node.left, count);
		}
		//if the name is more than the node.name traverse right
		if (name.compareToIgnoreCase(node.getData().getName()) > 0) {
			count += getCountByNameCounty(name, county,  node.right, count);
		}
		//if the name is the same
		if (name.equalsIgnoreCase(node.getData().getName())) { 
			//if the county is less than the node.county traverse left
			if (county.compareToIgnoreCase(node.getData().getCounty()) < 0) {
				count += getCountByNameCounty(name, county, node.left, count);
			}
			//if the county is more than the node.county traverse right
			if (county.compareToIgnoreCase(node.getData().getCounty()) > 0) {
				count += getCountByNameCounty(name, county, node.right, count);
			}
			//if the counties are the same then increment the counter
			if (county.equalsIgnoreCase(node.getData().getCounty())) { 
				count += node.getData().getCount() + getCountByNameCounty(name, county, node.left, count) + getCountByNameCounty(name, county, node.right, count);
			}
		}
		return count;
	}

	/**
	* Method that returns the fraction of babies with specified name as parameter in the
	* county specified as parameter. (number of babies found/ total number of babies in that county.)
	*@param name (name to be found) and county (county to be found)
	*@return fract (the fraction of babies found)
	*/
	public double getFractionByNameCounty (String name, String county) {
		//traverse the tree to find all the occurances of babies in that county
		double allBabies = (double) allBabiesInCounty(county);
		//if tree is empty return 0
		if (allBabies == 0) { 
			return 0;
		}
		double fract = 0;
		
		fract = (double) getCountByNameCounty(name, county); //get the number of babies for that name
		fract /= allBabies; 
		
		return fract;
	}
	
	/**
	* Helper method to find the number of babies in a specified county.
	* @param county (county to find)
	* @return count (number of babies found)
	*/
	protected int allBabiesInCounty (String county) {
		int count = 0;
		count += allBabiesInCounty (county, this.root);
		return count;
	}
	
	/**
	* Actual recursive implementation of allBabiesInCounty.
	* @param county (county to find) and node (node to begin recursion at)
	* @return count (number of babies found)
	*/
	protected int allBabiesInCounty (String county, Node<Name> node) {
		if (node == null) {
			return 0;
		}

		int count = 0;
		//traverse left
		count += allBabiesInCounty (county, node.left);
		
		if (node.getData().getCounty().equalsIgnoreCase(county)) {
			count += node.getData().getCount();
		}
		//traverse right
		count += allBabiesInCounty (county, node.right);
		return count;
	}
	
	
	/**
	 * This method compares two YearNames objects and returns true if the objects are equal
	 * or false if the objects are not equal.
	 * This method overrides the equals method from the Object class.
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @return true, false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
	        return true;
	    }
		
		if(!(obj instanceof YearNames)){
			return false;
		}

		YearNames other = (YearNames) obj;
		if(this.year == other.year) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method returns a string representation of the YearNames object
	 * @return a string representation of a YearNames object
	 */
	@Override 
	public String toString() {
		return "Year:" + this.year + ", " + "Number of names:" + numOfElements;
	}		
}