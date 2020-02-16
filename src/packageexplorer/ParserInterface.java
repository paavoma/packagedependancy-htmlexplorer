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
    
    public void readFile(String filepath);
    public void parseLine(String line, BufferedReader reader);
    public ArrayList<Package> getPackages();

   
    
    
}
