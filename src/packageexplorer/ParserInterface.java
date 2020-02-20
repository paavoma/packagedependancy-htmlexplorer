/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packageexplorer;

import java.io.BufferedReader;
import java.util.ArrayList;

/**
 *
 * @author crazm_000
 */
public interface ParserInterface {
	/**
	 * This method reads a file in a given filepath
	 * 
	 * @param filepath filepath to a file as String
	 */
	public void readFile(String filepath);

	/**
	 * This method goes through a single line on a file, and stores key information
	 * into Package class data structure
	 * 
	 * @param line   a line that is to be examined for key information
	 * @param reader the file reader that is needed to go through multiple lines
	 *               inside one category of key information (ex. multiple lines of
	 *               description)
	 */
	public void parseLine(String line, BufferedReader reader);

	/**
	 * This method returns the list of Package class objects
	 * 
	 * @return returns the ArrayList of Package objects
	 */
	public ArrayList<Package> getPackages();

	/**
	 * This method builds reverse dependencies for all packages in the list
	 */
	public void buildAllReverseDependencies();

}
