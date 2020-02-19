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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author crazm_000
 */
public class HtmlBuilder implements HtmlBuilderInterface {
    
    private String filePath="";
    private LinkedList<String> htmlCodeLines;
    private LinkedList<String> cssCodeLines;
    private ArrayList<Package> packages;

   
    public HtmlBuilder() {
        
    }

    @Override
    public boolean buildAllHtmlPages(ArrayList<Package> packages) {
        this.generateFilePath();
        try {
            Files.createDirectories(Paths.get(filePath));
        } catch (IOException ex) {
            Logger.getLogger(HtmlBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.packages=packages;
        this.cssCodeLines=loadDefaultCss();
        this.writeHtmlFile(cssCodeLines, "defaultStyle.css");
        this.buildIndexPage();
        for (Package p : packages) {
            this.buildPackagePage(p);
        }
        return true;
    }

    @Override
    //luo yhden packagen html sivun
    public boolean buildPackagePage(Package pack) {
        String pageName = generateHtmlFileName(pack.getName());
        htmlCodeLines = new LinkedList<>();
        
        htmlCodeLines.add(insertHeadingTagThree(insertLink("index")));
        htmlCodeLines.add(insertHeadingTagOne(pack.getName()));
        htmlCodeLines.add(insertHeadingTagTwo("Depends: "));
        insertDependancyList(pack.getDependencies());
        htmlCodeLines.add(insertHeadingTagTwo("Reverse dependencies: "));
        insertRevDependancyList(pack.getRevDependencies());
        htmlCodeLines.add(insertHeadingTagTwo("Description: "));
        insertDescription(pack.getDescription());
        insertDiv();
        insertHtmlAndBodyTags();
        writeHtmlFile(htmlCodeLines, pageName);
       

        return true;

    }
    private void insertDiv(){
        htmlCodeLines.addFirst("<div class=\"right\">");
        htmlCodeLines.add("</div>");
    }
    @Override
    public String insertTag(String stringToBeEnclosed, String htmlTag) {
        String htmlLine = "<" + htmlTag + ">" + stringToBeEnclosed + "</" + htmlTag + ">";
        return htmlLine;
    }

    
    public String insertHeadingTagOne(String stringToBeEnclosed) {

        return insertTag(stringToBeEnclosed, "h1");
    }
    private String insertHeadingTagTwo(String stringToBeEnclosed) {

        return insertTag(stringToBeEnclosed, "h2");
    }
    private String insertHeadingTagThree(String stringToBeEnclosed) {

        return insertTag(stringToBeEnclosed, "h3");
    }

    @Override
    //ensin käärii listaelementit linkkitägiin, sitten list tagiin
    public boolean insertDependancyList(ArrayList<String> dependencies) {
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
        return true;

    }

    private boolean checkIfDependancyIsPackage(String dependancy) {

        for (Package p : packages) {
            String packageName = p.getName();
            if (packageName.equals(dependancy)) {
                return true;
                //System.out.println("found dependancy!!!!!");
            }

        }
        return false;
    }

    @Override
    public boolean insertRevDependancyList(ArrayList<String> revDependencies) {
        htmlCodeLines.add("<ol>");
        for (String revDependancy : revDependencies) {
            htmlCodeLines.add(insertTag(insertLink(revDependancy), "li"));
        }
        htmlCodeLines.add("</ol>");
        return true;
    }

    @Override
    public boolean insertDescription(ArrayList<String> description) {
        for (String descriptionLine : description) {
            htmlCodeLines.add(insertTag(descriptionLine, "p"));
        }
        return true;
    }

    @Override
    public void insertHtmlAndBodyTags() {
        htmlCodeLines.addFirst("<body>");
        htmlCodeLines.addFirst("</head>");
        htmlCodeLines.addFirst("<link rel=\"stylesheet\" type=\"text/css\" href=\"defaultStyle.css\">");
        htmlCodeLines.addFirst("<head>");
        
        htmlCodeLines.addFirst("<html>");
        
        
        htmlCodeLines.addFirst("<!DOCTYPE html>");
        htmlCodeLines.add("</body>");
        htmlCodeLines.add("</html>");
    }

    @Override
    public void writeHtmlFile(LinkedList<String> htmlCodeLines, String pageName) {
        
        //String filepath = "C:\\Koodiprojektit\\PackageInfoExplorer\\html\\" + pageName;
        
        String filepath = this.filePath + pageName;
        
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
                br.write(line + System.getProperty("line.separator"));
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

    //Sorts packages alphabetically and builds index.html page
    private void buildIndexPage() {
        //loads up package names
        htmlCodeLines = new LinkedList<>();
        ArrayList<String> packageNames = new ArrayList<>();

        for (Package p : packages) {
            packageNames.add(p.getName());
        }
        //sorts the names alphabetically
        Collections.sort(packageNames);
        
        
        htmlCodeLines.add(this.insertHeadingTagOne("Index page"));
        htmlCodeLines.add("<ol>");
        for (String packageName : packageNames) {
            htmlCodeLines.add(this.insertTag(this.insertLink(packageName), "li"));
        }
        htmlCodeLines.add("</ol>");
        insertDiv();
        insertHtmlAndBodyTags();
        writeHtmlFile(htmlCodeLines, "index.html");
    }

   

    @Override
    public String insertLink(String packageNameToLink) {
        String htmlLine = "<a href='" + generateHtmlFileName(packageNameToLink) + "'>" + packageNameToLink + "</a>";
        return htmlLine;

    }

    @Override
    public String generateHtmlFileName(String packageName) {
        String htmlFileName = packageName.replace(".", "_");
        htmlFileName = htmlFileName + ".html";
        return htmlFileName;

    }

    @Override
    public String insertParagraph(String stringToBeEnclosed) {
        return insertTag(stringToBeEnclosed, "p");
    }

    private LinkedList<String> loadDefaultCss() {
        LinkedList<String> cssCodeLines = new LinkedList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "defaultStyle.css"));
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

    
    
    public void isOperatingSystemDebian(){
        if(PacketExplorer.PROJECTPATH.charAt(0) == '/'){
            PacketExplorer.ISDEBIAN=true;
        }else{
            PacketExplorer.ISDEBIAN=false;
        }
        
    }

    private void generateFilePath() {
        if(PacketExplorer.ISDEBIAN){
            filePath=System.getProperty("user.dir") + "/html/";
        }else{
        filePath=System.getProperty("user.dir") + "\\html\\";
        }
    }
    public void openIndexPageWithBrowser() throws URISyntaxException, IOException{
        //pitää kääntää ur
        String filepathToOpen = filePath + "index.html";
        File file=new File(filepathToOpen);
        URI path=file.toURI();

        if (Desktop.isDesktopSupported()) {
            // Windows
            Desktop.getDesktop().browse(path);
        } else {
            // Ubuntu
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("/usr/bin/firefox -new-window " + filepathToOpen);
        }
    }

}
