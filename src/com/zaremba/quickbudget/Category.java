/**
 * Category class represents a category in the monthly bugdet.
 * It contains a retailer list and a goal for the month.
 * It is created when the Budget Object is instantiated.
 * The Categories are found in the categories.csv file along with the goal budget.
 * The added retailers is an ongoing list that will be stored in a file of the categories name.
 */

package com.zaremba.quickbudget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Category {
    private String name;
    private double goal;
    private ArrayList<Retailer> retailers;
    private File retailListFile;

    public String getName() {
        return name;
    }

    public double getGoal() {
        return goal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Category) {
            Category other = (Category) obj;
            if (this.name.equals(other.name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Creates the category object and fills the list of retailers from the file in the Categories Folder
     * @param name The name of the category
     * @param goal The money goal for a month of spending
     */
    public Category(String name, double goal) {
        this.name = name;
        this.goal = goal;
        retailers = new ArrayList<>();
        retailListFile = new File("Categories/" + name + ".csv");
        if(!retailListFile.exists()){
            try {
                retailListFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                Scanner scanner = new Scanner(retailListFile);
                while (scanner.hasNext()) {
                    retailers.add(new Retailer(scanner.nextLine(), this));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
