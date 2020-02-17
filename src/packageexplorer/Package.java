/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packageexplorer;

import java.util.ArrayList;

/**
 *
 * @author crazm_000
 */

//This class stores 
public class Package {

    private String name;
    private ArrayList<String> description;
    private ArrayList<String> dependencies;
    private ArrayList<String> revDependencies;

    
    public Package() {
        dependencies = new ArrayList<>();
        description = new ArrayList<>();
        revDependencies = new ArrayList<>();
    }

    public ArrayList<String> getDependencies() {
        return dependencies;
    }

    public void buildReverseDependencies(ArrayList<Package> packages) {
        String currentName = this.getName();
      
        for (Package p : packages) {
            ArrayList<String>  dependenciesToCompare = p.getDependencies();
            for (String s : dependenciesToCompare) {
                if (s.equals(currentName)) {
                    this.revDependencies.add(p.getName());
                }
            }
           
        }
    }

    public void printReverseDependencies() {
        for (String s : revDependencies) {
            System.out.println(s);
        }
    }

    public void printDependencies() {
        for (String s : dependencies) {
            System.out.println(s);
        }
    }

    public void addDependency(String dependency) {
        dependencies.add(dependency);
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public void addDescription(String description) {
        this.description.add(description);
    }

    public void printDescription() {
        for (String s : description) {
            System.out.println(s);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void printName() {
        System.out.println(name);
    }
    public ArrayList<String> getRevDependencies() {
        return revDependencies;
    }


}
