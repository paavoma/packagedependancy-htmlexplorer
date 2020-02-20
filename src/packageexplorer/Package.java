/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packageexplorer;

import java.util.ArrayList;

/**
 * This class contains the data structure that is used for storing package
 * information that is needed for the HTML-interface
 * 
 * @author Paavo Mattila
 */
public class Package {

	private String name;
	private ArrayList<String> description;
	private ArrayList<String> dependencies;
	private ArrayList<String> revDependencies;

	/**
	 * This is the default constructor for the class
	 */
	public Package() {
		dependencies = new ArrayList<String>();
		description = new ArrayList<String>();
		revDependencies = new ArrayList<String>();
	}

	/**
	 * This method builds reverse package dependencies by going through the given
	 * Package list and comparing it to the current packages.
	 * 
	 * @param packages contains all the packages to be compared to
	 */
	public void buildReverseDependencies(ArrayList<Package> packages) {
		String currentName = this.getName();

		for (Package p : packages) {
			ArrayList<String> dependenciesToCompare = p.getDependencies();
			for (String s : dependenciesToCompare) {
				if (s.equals(currentName)) {
					this.revDependencies.add(p.getName());
				}
			}
		}
	}

	/**
	 * This method returns list of reverse dependencies from this Package
	 * 
	 * @return is an ArrayList of reverse dependencies as String
	 */

	public ArrayList<String> getRevDependencies() {
		return revDependencies;
	}

	/**
	 * This method prints package's reverse dependencies. Used for debugging and
	 * testing
	 */
	public void printReverseDependencies() {
		for (String s : revDependencies) {
			System.out.println(s);
		}
	}

	/**
	 * This method adds a dependency into the Package dependency list
	 * 
	 * @param dependency dependency name to to be added
	 */

	public void addDependency(String dependency) {
		dependencies.add(dependency);
	}

	/**
	 * This method returns a list of dependencies as String from this Package
	 * 
	 * @return is an ArrayList of dependencies as String
	 */
	public ArrayList<String> getDependencies() {
		return dependencies;
	}

	/**
	 * This method prints package's dependencies. Used for debugging and testing
	 */

	public void printDependencies() {
		for (String s : dependencies) {
			System.out.println(s);
		}
	}

	/**
	 * This method adds a line of description to the Package description list
	 * 
	 * @param description description text to be added into the Package
	 */
	public void addDescription(String description) {
		this.description.add(description);
	}

	/**
	 * This method returns a list of description lines of the package
	 * 
	 * @return list of description lines
	 */
	public ArrayList<String> getDescription() {
		return description;
	}

	/**
	 * This method prints in console the description files this package has. Used
	 * for testing and debugging.
	 */
	public void printDescription() {
		for (String s : description) {
			System.out.println(s);
		}
	}

	/**
	 * This method sets the packages name attribute
	 * 
	 * @param name the name to be inputted
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * This method returns the name of the package
	 * 
	 * @return name of he package as String
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method prints in console the name of the package. Used for testing and
	 * debugging.
	 */
	public void printName() {
		System.out.println(name);
	}

}
