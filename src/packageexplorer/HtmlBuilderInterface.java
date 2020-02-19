/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packageexplorer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author crazm_000
 */
public interface HtmlBuilderInterface {
    
    public boolean buildAllHtmlPages(ArrayList<Package> packages);
    public boolean buildPackagePage(Package pack);
    public String insertTag(String stringToBeEnclosed, String htmlTag);
    public boolean insertDependancyList(ArrayList<String> dependencies);
    public boolean insertRevDependancyList(ArrayList<String> revDependencies);
    public String insertParagraph(String stringToBeEnclosed);
    public boolean insertDescription(ArrayList<String> description);
    public void insertHtmlAndBodyTags();
    
    public void writeHtmlFile(LinkedList<String> htmlCodeLines, String htmlFileName);
    
    public String insertLink(String packageNameToLink);
    public String generateHtmlFileName(String packageName);
    public void isOperatingSystemDebian();
    public void openIndexPageWithBrowser() throws URISyntaxException, IOException;
    
}
