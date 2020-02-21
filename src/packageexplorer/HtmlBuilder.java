/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packageexplorer;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * This class builds and writes files (html, css) and creates required folder
 * structure required to run the web pages. Uses Package class as data structure
 * 
 * @author Paavo Mattila
 *
 */
public class HtmlBuilder implements HtmlBuilderInterface {
	/**
	 * Absolute filepath of html-packages pages
	 */
	private String pathForHtmlPackages = "";
	/**
	 * Absolute filepath for css file location
	 */
	private String pathForCss = "";
	/**
	 * Absolute filepath for index.html location
	 */
	private String pathForIndexPage = "";
	/**
	 * Absolute filepath for project location
	 */
	private String projectFilePath = "";
	private String styleFileName = "defaultStyle.css";
	/**
	 * short path to subfolder
	 */
	private String folderToPackages = "packages/";
	/**
	 * A list of code lines to be written as a file
	 */
	private LinkedList<String> htmlCodeLines;
	/**
	 * A list of code lines to be written as a file
	 */
	private LinkedList<String> cssCodeLines;
	/**
	 * A list of Package objects
	 */
	private ArrayList<Package> packages;

	/**
	 * This is the default constructor for the class
	 */
	public HtmlBuilder() {

	}

	/**
	 * This method builds all the required web pages and files from the given
	 * Package list
	 * 
	 * @param packages is an ArrayList of packages, each containing information for
	 *                 the web page
	 */
	public void buildAllHtmlPages(ArrayList<Package> packages) {
		projectFilePath = findProjectRootPath();
		this.generateFilePath();

		try {
			Files.createDirectories(Paths.get(pathForHtmlPackages));
			Files.createDirectories(Paths.get(pathForCss));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.packages = packages;
		this.cssCodeLines = loadDefaultCss();
		this.writeHtmlFile(cssCodeLines, styleFileName);
		this.buildIndexPage();
		for (Package p : packages) {
			this.buildPackagePage(p);
		}

	}

	/**
	 * This method builds one .html page from given package
	 * 
	 * @param pack is a single Package data structure, from which information is
	 *             extracted
	 */
	public void buildPackagePage(Package pack) {
		String pageName = generateHtmlFileName(pack.getName());
		htmlCodeLines = new LinkedList<String>();

		htmlCodeLines.add(insertHeadingTagThree(insertLink("index")));
		htmlCodeLines.add(insertHeadingTagOne(pack.getName()));
		htmlCodeLines.add(insertHeadingTagTwo("Depends: "));
		insertDependancyList(pack.getDependencies());
		htmlCodeLines.add(insertHeadingTagTwo("Reverse dependencies: "));
		insertRevDependancyList(pack.getRevDependencies());
		htmlCodeLines.add(insertHeadingTagTwo("Description: "));
		insertDescription(pack.getDescription());
		insertDiv();
		insertHtmlAndBodyTags(pack.getName());
		writeHtmlFile(htmlCodeLines, pageName);

	}

	/**
	 * This method inserts predefined div-tags, so CSS functions properly
	 */
	private void insertDiv() {
		htmlCodeLines.addFirst("<div class=\"right\">");
		htmlCodeLines.add("</div>");
	}

	/**
	 * This method surrounds the given String with given tag text, used to build
	 * html code
	 * 
	 * @param stringToBeEnclosed the text that needs to be enclosed
	 * @param htmlTag            the html-code tag that surrounds the text
	 * @return is the surrounded codeline
	 */
	public String insertTag(String stringToBeEnclosed, String htmlTag) {
		String htmlLine = "<" + htmlTag + ">" + stringToBeEnclosed + "</" + htmlTag + ">";
		return htmlLine;
	}

	/**
	 * This method inserts a h1 heading tag on the given text
	 * 
	 * @param stringToBeEnclosed String text to be surrounded with the tag
	 * @return text with surrounded tags
	 */

	private String insertHeadingTagOne(String stringToBeEnclosed) {
		return insertTag(stringToBeEnclosed, "h1");
	}

	/**
	 * This method inserts a h2 heading tag on the given text
	 * 
	 * @param stringToBeEnclosed String text to be surrounded with the tag
	 * @return text with surrounded tags
	 */
	private String insertHeadingTagTwo(String stringToBeEnclosed) {
		return insertTag(stringToBeEnclosed, "h2");
	}

	/**
	 * This method inserts a h3 heading tag on the given text
	 * 
	 * @param stringToBeEnclosed String text to be surrounded with the tag
	 * @return text with surrounded tags
	 */
	private String insertHeadingTagThree(String stringToBeEnclosed) {
		return insertTag(stringToBeEnclosed, "h3");
	}

	/**
	 * This method builds a html ol-list to htmlCodeLines list from given String
	 * list with elements
	 * 
	 * @param dependencies is an ArrayList of package's dependencies as String
	 */
	public void insertDependancyList(ArrayList<String> dependencies) {
		boolean foundPackage = false;
		htmlCodeLines.add("<ol>");
		for (String dependancy : dependencies) {
			foundPackage = checkIfDependancyIsPackage(dependancy);

			if (foundPackage == false) {
				htmlCodeLines.add(insertTag(dependancy, "li"));
			} else {
				htmlCodeLines.add(insertTag(insertLink(dependancy), "li"));
			}
			foundPackage = false;
		}
		htmlCodeLines.add("</ol>");
	}

	/**
	 * This method checks if a dependancy of a Package is a Package that exists in
	 * the packages list
	 * 
	 * @param dependancy is the dependancy to be checked
	 * @return true if there is a package in the packagelist with the same name as
	 *         dependancy
	 */
	private boolean checkIfDependancyIsPackage(String dependancy) {

		for (Package p : packages) {
			String packageName = p.getName();
			if (packageName.equals(dependancy)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method builds an html ol-list to htmlCodeLines list from given String
	 * list with elements
	 * 
	 * @param revDependencies is an ArrayList of package's reverse dependencies as
	 *                        String
	 *
	 */
	public void insertRevDependancyList(ArrayList<String> revDependencies) {
		htmlCodeLines.add("<ol>");
		for (String revDependancy : revDependencies) {
			htmlCodeLines.add(insertTag(insertLink(revDependancy), "li"));
		}
		htmlCodeLines.add("</ol>");
	}

	/**
	 * This methods enters a series of paragraph lines from Package class
	 * description
	 * 
	 * @param description a description of the packages function as String
	 */

	public void insertDescription(ArrayList<String> description) {
		for (String descriptionLine : description) {
			htmlCodeLines.add(insertParagraph(descriptionLine));
		}
	}

	/**
	 * This method adds html code lines to htmlCodeLines ArrayList, such as body,
	 * head, and css references
	 * 
	 * @param title this is the title text for the page as String
	 */
	public void insertHtmlAndBodyTags(String title) {
		htmlCodeLines.addFirst("<body>");
		htmlCodeLines.addFirst("</head>");
		htmlCodeLines.addFirst("<meta charset=\"UTF-8\">");
		htmlCodeLines.addFirst("<title>" + title + "</title>");
		if (title.contentEquals("Index page")) {
			htmlCodeLines.addFirst("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/" + styleFileName + "\">");
		} else {
			htmlCodeLines.addFirst("<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/" + styleFileName + "\">");
		}

		htmlCodeLines.addFirst("<head>");
		htmlCodeLines.addFirst("<html lang=\"en-GB\">");
		htmlCodeLines.addFirst("<!DOCTYPE html>");
		htmlCodeLines.add("</body>");
		htmlCodeLines.add("</html>");
	}

	/**
	 * This method creates file and writes the htmlCodeLines LinkedList inside it
	 * 
	 * @param htmlCodeLines this is the list of html code lines to be written into a
	 *                      file
	 * @param pageName      name of the web page or file as String
	 */
	public void writeHtmlFile(LinkedList<String> htmlCodeLines, String pageName) {
		String filepath = "";
		if (pageName.equals("index.html")) {
			filepath = this.pathForIndexPage + pageName;
		} else if (pageName.equals(styleFileName)) {
			filepath = this.pathForCss + pageName;
		} else {
			filepath = this.pathForHtmlPackages + pageName;
		}
		File fileToWrite = new File(filepath);
		FileWriter fr = null;
		BufferedWriter br = null;
		try {
			fileToWrite.createNewFile();
		} catch (IOException ioe) {
			System.out.println("Error while Creating File" + ioe);
		}
		try {
			fr = new FileWriter(fileToWrite);
			br = new BufferedWriter(fr);
			for (String line : htmlCodeLines) {
				br.write(line + System.getProperty("line.separator")); // this is required to get line break for each
																		// line
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Sorts packages alphabetically and builds index.html page
	/**
	 * This method generates an index.html page with a list of packages and links to
	 * them. The method collects package names, sorts them and forms an ordered list
	 * out of them
	 */
	private void buildIndexPage() {
		// loads up package names
		htmlCodeLines = new LinkedList<String>();
		ArrayList<String> packageNames = new ArrayList<String>();

		for (Package p : packages) {
			packageNames.add(p.getName());
		}
		// sorts the names alphabetically
		Collections.sort(packageNames);

		htmlCodeLines.add(this.insertHeadingTagOne("Index page"));
		htmlCodeLines.add("<ol>");
		for (String packageName : packageNames) {
			htmlCodeLines.add(this.insertTag(this.insertLink(folderToPackages + packageName), "li"));
		}
		htmlCodeLines.add("</ol>");
		insertDiv();
		insertHtmlAndBodyTags("Index page");
		writeHtmlFile(htmlCodeLines, "index.html");
	}

	/**
	 * This method surrounds a given String with a link tag and gives it a href link
	 * 
	 * @param packageNameToLink name of the package to be linked
	 * @return text that is surrounded with link tags
	 */
	public String insertLink(String packageNameToLink) {
		String htmlLine = "";
		if (packageNameToLink.equals("index")) {
			htmlLine = "<a href='../" + generateHtmlFileName(packageNameToLink) + "'>" + packageNameToLink + "</a>";
		} else {
			htmlLine = "<a href='" + generateHtmlFileName(packageNameToLink) + "'>" + packageNameToLink + "</a>";
		}
		return htmlLine;
	}

	/**
	 * This method generates a html filename from the given string
	 * 
	 * @param pageName the page name that will get an .html extension
	 * @return is the filename as a String
	 */
	public String generateHtmlFileName(String pageName) {
		String htmlFileName = pageName.replace(".", "_");
		htmlFileName = htmlFileName + ".html";
		return htmlFileName;

	}

	/**
	 * This method surrounds the text with paragraph tags
	 * 
	 * @param stringToBeEnclosed this is the string that is to be enclosed
	 * @return text surrounds with paragraph tags
	 */
	public String insertParagraph(String stringToBeEnclosed) {
		return insertTag(stringToBeEnclosed, "p");
	}

	/**
	 * This method reads defaultStyle.css and adds the lines it contains to
	 * htmCodelines LinkedList. defaultStyle.css is located in the root folder of
	 * the project.
	 * 
	 * @return a LinkedList with Strings containing code lines
	 */
	private LinkedList<String> loadDefaultCss() {
		LinkedList<String> cssCodeLines = new LinkedList<String>();
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(projectFilePath + "/defaultStyle.css"));
			System.out.println(projectFilePath);
			String line = reader.readLine();
			while (line != null) {
				cssCodeLines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cssCodeLines;
	}

	/**
	 * This method checks if the user is using a Debian based OS. This is required,
	 * because folder path information has different syntax
	 * 
	 * @return true if current operating system is debian. The address starts with /
	 *         instead of C:\etc
	 */

	public boolean isOperatingSystemDebian() {
		if (PacketExplorer.PROJECTPATH.charAt(0) == '/') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method finds the project root path, where it has been executed based on
	 * the OS
	 * 
	 * @return absolute filepath of the project
	 */
	public String findProjectRootPath() {

		URL jarLocationUrl = HtmlBuilder.class.getProtectionDomain().getCodeSource().getLocation();
		String jarLocation = new File(jarLocationUrl.toString()).getParent();
		try {
			jarLocation = URLDecoder.decode(jarLocation, "UTF-8"); // decodes into UTF-8, so spaces and special
																	// characters show up on path
		} catch (UnsupportedEncodingException e) {
			System.out.println("decoding failed");
			e.printStackTrace();
		}
		// because result path differs if used on different OS.
		String lastSevenChar = jarLocation.substring(jarLocation.length() - 7);
		if (PacketExplorer.ISDEBIAN) {
			jarLocation = jarLocation.substring(5);
		} else if (lastSevenChar.contains("target")) { // so that program can be used in IDE aswell
			jarLocation = new File("").getAbsolutePath();
		} else {
			jarLocation = jarLocation.substring(6);
		}
		return jarLocation;
	}

	/**
	 * This method generates filepaths that are needed to generate subfolders for
	 * the web pages. U
	 */
	private void generateFilePath() {

		if (PacketExplorer.ISDEBIAN) {
			pathForHtmlPackages = projectFilePath + "/html/packages/";
			pathForIndexPage = projectFilePath + "/html/";
			pathForCss = projectFilePath + "/html/css/";

		} else {
			pathForHtmlPackages = projectFilePath + "\\html\\packages\\";
			pathForIndexPage = projectFilePath + "\\html\\";
			pathForCss = projectFilePath + "\\html\\css\\";
		}
	}

	/**
	 * This method opens the index.html file in a default browser that has been
	 * generated by the program.
	 */
	public void openIndexPageWithBrowser() {

		String filepathToOpen = pathForIndexPage + "index.html";
		File file = new File(filepathToOpen);
		URI path = file.toURI();

		if (Desktop.isDesktopSupported()) {
			// Windows
			try {
				Desktop.getDesktop().browse(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Debian or Ubuntu
			Runtime rt = Runtime.getRuntime();
			try {
				rt.exec("/usr/bin/firefox -new-window " + filepathToOpen); // it is not default, but most people have
																			// firefox with Linux OS. So sorry for mac
																			// users
			} catch (IOException e) {
				System.out.println("So sorry if you have MAC");
				e.printStackTrace();
			}
		}
	}
}
