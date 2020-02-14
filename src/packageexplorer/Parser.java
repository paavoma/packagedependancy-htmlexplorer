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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author crazm_000
 */
public class Parser implements ParserInterface {

    private Package p;
    private String line;
    private BufferedReader reader;
    private ArrayList<Package> packages;

    public Parser() {
        String line = null;
        packages = new ArrayList<Package>();
    }

    public void readFile(String filepath) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                // read next line
                parseLine(line, reader);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Package p : packages) {
            p.buildReverseDependencies(packages);
            
        }
        for (Package p : packages) {
            
            System.out.println("Package name: ");
            System.out.println(p.getName());
            System.out.println("Depends: ");
            p.printDependencies();
            System.out.println("Reverse dependencies: ");
            p.printReverseDependencies();
            //System.out.println("Description: ");
            //p.printDescription();
        }

    }

    @Override
    public void parseLine(String line, BufferedReader reader) {
        try {

            String[] lineToAdd = null;
            if (line.contains("Package: ")) {
                p = new Package();
                packages.add(p);
                lineToAdd = line.split("Package: ");
                if (lineToAdd.length == 2) {
                    p.setName(lineToAdd[1]);
                    //System.out.println("Package name: " + attribute[1]);
                }
            }

            if (line.contains("Depends: ")) {
                lineToAdd = line.split("Depends: ");
                if (lineToAdd.length >= 2) {
                    lineToAdd = lineToAdd[1].split(", ");
                    //System.out.println("Depends: ");
                    int i = 0;
                    while (lineToAdd.length > i) {
                        //karsii versionumeron perästä
                        int index = lineToAdd[i].indexOf(" ");
                        if (index > 0) {
                            lineToAdd[i] = lineToAdd[i].substring(0, index);
                        }
                        p.addDependency(lineToAdd[i]);
                        //System.out.println(attribute[i]);
                        i++;
                    }
                }

            }

            if (line.contains("Description: ")) {
                lineToAdd = line.split("Description: ");

                if (lineToAdd.length == 2) {

                    //System.out.println("Description: " + attribute[1]);
                    p.addDescription(lineToAdd[1]);
                }
                line = reader.readLine();
                char firstletter = ' ';
                if (line.trim().length() > 0) {
                    firstletter = line.charAt(0);
                }
                while (firstletter == ' ') {
                    p.addDescription(line);
                    //System.out.println(line);
                    line = reader.readLine();

                    if (line.trim().length() > 0) {
                        firstletter = line.charAt(0);
                    }

                }

            }

        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
