package project5;

/**
 * This class constructs and holds all methods for an instance of a Name object.
 * It varifies that a Name object is created with specific criteria and implements the
 * comparable interface and overrides both the equals() method and the toString() method.
 * @author Sarah Wardles
 *
 */
public class Name implements Comparable<Name> {
	
	protected String name = null;
	protected String gender = null;
	protected int count = 0;
	protected String county = null;
	
	/**
	 * This is the constructor for the Name object class. There is no default constructor.
	 * @param name (must be a valid string)
	 * @param gender (must be either f or m)
	 * @param count (must be a ppositive integer)
	 * @param county (must be a valid string)
	 * @throws IllegalArgumentException (throws if any of the criteria for validating the parameters is wrong)
	 */
	public Name (String name, String gender, int count, String county) {
		// make sure th estring is not empty/null
		if (name.length() < 1 || name.equals("")) {
			throw new IllegalArgumentException("The program must take a valid gender as a parameter.");
		} 
		if(name == null){
			throw new NullPointerException("The name attribute must not be null.");
		}
		// make sure the gender is a valid key f/m
		if (!(gender.equals("f") || gender.equals("m") || gender.equals("F") || gender.equals("M"))) {
			throw new IllegalArgumentException("The program must take a valid gender as a parameter ");
		} 
		
		// make sure the count is positive
		if (count < 0) {
			throw new IllegalArgumentException("The program must take a positive count as a parameter.");
		}
		
		// make sure the county is a valid string
		if (county.equals("")) {
			throw new IllegalArgumentException("The program must take a valid county as a parameter.");
		}
		
		this.name = name;
		this.gender = gender;
		this.count = count;
		this.county = county;
	}
	
	/**
	 * Helper method that returns the name of a Name object. 
	 * @return this.name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Helper method that returns the gender of the Name object.
	 * @return this.gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Helper method that returns the count of the name object.
	 * @return this.count
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * Helper method that returns the county of the name object.
	 * @return this.county
	 */
	public String getCounty() {
		return county;
	}

	//implement the Comparable interface
	/**
	 * This method returns an integer representation of 1,0,-1 in comparing two Name objects. 
	 * This method overrides the compareTo method in the Comparable interface
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @return 1, 0 ,-1
	 */
	@Override
	public int compareTo(Name n) {
		if (this.name.compareToIgnoreCase(n.name) != 0) { 
			return this.name.compareToIgnoreCase(n.name);
		} else { 
			if (this.county.compareToIgnoreCase(n.county) != 0) {
				return this.county.compareToIgnoreCase(n.county);
			} else { 
				if (this.gender.compareToIgnoreCase(n.gender) != 0) { 
					return this.gender.compareToIgnoreCase(n.gender);
				}
			}
		}
		//return 0 if ultimately equal
		return 0;
	}
	
	/**
	 * This method compares two Name objects and returns true if the objects are equal
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
		
		if (obj instanceof Name) {
			Name n = (Name) obj;
			
			if (this.name.equalsIgnoreCase(n.name)) {
				if (this.count == n.count) {
					if (this.gender.equalsIgnoreCase(n.gender)) {
						if (this.county.equalsIgnoreCase(n.county)) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Helper method that checks if two Name objects are equal. Does not compare their counts.
	 * @return boolean true/false
	 */
	public boolean equalsIgnoreCount(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj instanceof Name) {
			Name other = (Name) obj;
			
			if (this.name.equals(other.name)) {
				if (this.gender.equalsIgnoreCase(other.gender)) {
					if (this.county.equalsIgnoreCase(other.county)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * This method returns a string representation of this Name object.
	 * @return string representation of this Name object
	 */
	@Override
	public String toString() {
		return "Name:" + this.name + ", " + "County:" + this.county + ", " + "Gender:" + this.gender + ", " + "Count:" + this.count;
	}	
}