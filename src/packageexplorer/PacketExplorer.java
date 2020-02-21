
package packageexplorer;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * This is the Main class for the program. It reads a specific file in the file
 * system, collects package information it uses. Then it generates HTML-pages
 * out of that information and opens them in users default browser. The
 * generated pages can be found on the root of the project in /html folder as
 * well
 * 
 * @author Paavo Mattila
 */
public class PacketExplorer {

	public final static String PROJECTPATH = System.getProperty("user.dir");
	public static boolean ISDEBIAN = false;

	/**
	 * This is the main method.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		ParserInterface parser = new Parser();
		HtmlBuilderInterface htmlBuilder = new HtmlBuilder();
		ISDEBIAN = htmlBuilder.isOperatingSystemDebian();
		if (ISDEBIAN) {
			parser.readFile("/var/lib/dpkg/status");
		} else {
			parser.readFile("status.real");
		}

		parser.buildAllReverseDependencies();
		htmlBuilder.buildAllHtmlPages(parser.getPackages());
		try {
			htmlBuilder.openIndexPageWithBrowser();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}