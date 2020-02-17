/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packageexplorer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author crazm_000
 */
public class HtmlBuilder implements HtmlBuilderInterface {

    private LinkedList<String> htmlCodeLines;

    //konstruktoriin packages arraylist
    public HtmlBuilder() {

    }

    @Override
    public boolean buildAllHtmlPages(ArrayList<Package> packages) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    //luo yhden packagen html sivun
    public boolean buildPackagePage(Package pack) {
        String pageName=generateHtmlFileName(pack.getName());
        htmlCodeLines = new LinkedList<>();
        
        htmlCodeLines.add(insertHeadingTag(pack.getName()));
        htmlCodeLines.add(insertParagraph("Depends: "));
        insertDependancyList(pack.getDependencies());
        htmlCodeLines.add(insertParagraph("Reverse dependencies: "));
        insertRevDependancyList(pack.getRevDependencies());
        htmlCodeLines.add(insertParagraph("Description: "));
        insertDescription(pack.getDescription());
        insertHtmlAndBodyTags();
        writeHtmlFile(htmlCodeLines, pageName);
        
        for (String line : htmlCodeLines) {
            System.out.println(line);
        }
        

        return true;

    }

    @Override
    public String insertTag(String stringToBeEnclosed, String htmlTag) {
        String htmlLine = "<" + htmlTag + ">" + stringToBeEnclosed + "</" + htmlTag + ">";
        return htmlLine;
    }

    @Override
    public String insertHeadingTag(String stringToBeEnclosed) {

        return insertTag(stringToBeEnclosed, "h1");
    }

    @Override
    //ensin käärii listaelementit linkkitägiin, sitten list tagiin
    public boolean insertDependancyList(ArrayList<String> dependencies) {
        htmlCodeLines.add("<ol>");
        for (String dependancy : dependencies) {
            htmlCodeLines.add(insertTag(insertLink(dependancy), "li"));
        }
        htmlCodeLines.add("</ol>");
        return true;

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
        for(String descriptionLine : description){
            htmlCodeLines.add(insertTag(descriptionLine, "p"));
        }
        return true;
    }
    @Override
    public void insertHtmlAndBodyTags() {
        htmlCodeLines.addFirst("<body>");
        htmlCodeLines.addFirst("<html>");
        htmlCodeLines.addFirst("<!DOCTYPE html>");
        htmlCodeLines.add("</body>");
        htmlCodeLines.add("</html>");
    }

    @Override
    public void writeHtmlFile(LinkedList<String> htmlCodeLines, String pageName) {
        boolean fileCreateSuccesful=false;
        String filepath="C:\\Koodiprojektit\\PackageInfoExplorer\\html\\" + pageName;
        File fileToWrite = new File(filepath);
        FileWriter fr = null;
        BufferedWriter br = null;
        String dataLine=htmlCodeLines.get(0)+System.getProperty("line.separator");
        try{
            fileCreateSuccesful=fileToWrite.createNewFile();
        }catch(IOException ioe){
            System.out.println("Error while Creating File" + ioe);
        }
        try{
            
            fr = new FileWriter(fileToWrite);
            br = new BufferedWriter(fr);
            for(String line: htmlCodeLines){
                br.write(line+System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    @Override
    public boolean insertListTag(String stringToBeEnclosed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    

    

    

   
}
