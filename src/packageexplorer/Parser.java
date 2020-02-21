/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packageexplorer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class reads a file and parses key information out of it. Forms a list of
 * Package's which is used to generate the html-pages in HtmlBuilder class
 * 
 * @author Paavo Mattila
 */
public class Parser implements ParserInterface {

	private Package packageToIterate;
	private BufferedReader reader;
	private ArrayList<Package> packages;

	/**
	 * This is the default contructor for the class
	 */
	public Parser() {
		packages = new ArrayList<Package>();
	}

	/**
	 * This method reads a file in a given filepath
	 * 
	 */
	public void readFile(String filepath) {

		try {
			reader = new BufferedReader(new FileReader(filepath));
			String line = reader.readLine();
			while (line != null) {

				parseLine(line, reader);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method builds reverse dependencies for all packages in the list
	 */
	public void buildAllReverseDependencies() {
		for (Package p : packages) {
			p.buildReverseDependencies(packages);

		}
	}

	/**
	 * This method goes through a single line on a file, and stores key information
	 * into Package class data structure
	 * 
	 * @param line   a line that is to be examined for key information
	 * @param reader the file reader that is needed to go through multiple lines
	 *               inside one category of key information (ex. multiple lines of
	 *               description)
	 */
	public void parseLine(String line, BufferedReader reader) {

		String[] lineToAdd = null;
		if (line.contains("Package: ")) {
			packageToIterate = new Package();
			packages.add(packageToIterate);
			lineToAdd = line.split("Package: ");
			if (lineToAdd.length == 2) {
				packageToIterate.setName(lineToAdd[1]);
			}
		}

		else if (line.contains("Depends: ") && !line.contains("Pre-Depends: ")) {
			lineToAdd = line.split("Depends: ");
			if (lineToAdd.length >= 2) {
				lineToAdd = lineToAdd[1].split("[,] |[|]");
				int i = 0;
				while (lineToAdd.length > i) {
					int index = lineToAdd[i].indexOf(" ");
					if (index > 0) {
						lineToAdd[i] = lineToAdd[i].substring(0, index);
					}
					packageToIterate.addDependency(lineToAdd[i]);

					i++;
				}
			}

		}

		else if (line.contains("Description: ")) {
			lineToAdd = line.split("Description: ");

			if (lineToAdd.length == 2) {
				packageToIterate.addDescription(lineToAdd[1]);
			}
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			char firstletter = ' ';
			if (line.trim().length() > 0) {
				firstletter = line.charAt(0);
			}
			while (firstletter == ' ' && line.length() > 1) {
				packageToIterate.addDescription(line);
				try {
					line = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (line.trim().length() > 0) {
					firstletter = line.charAt(0);
				}

			}

		}

	}

	/**
	 * This method returns the list of Package class objects
	 * 
	 * @return returns the ArrayList of Package objects
	 */
	public ArrayList<Package> getPackages() {
		return packages;
	}

}
