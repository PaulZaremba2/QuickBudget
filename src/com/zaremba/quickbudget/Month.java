package com.zaremba.quickbudget;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Month {
    private String name;
    private ArrayList<Entry> entries;
    private ArrayList<Category> categories;

    public Month(String name, ArrayList<Entry> entries, ArrayList<Category> categories) {
        this.name = name;
        this.entries = entries;
        this.categories = categories;
        createEntryFile();
    }

    public Month(String name) {
        populateEntriesFromFile(name);
    }

    private void createEntryFile() {
        File file = new File("Months/" + name + ".csv");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            boolean first = true;
            for (Category category : categories) {
                if(first){
                    writer.write(category.getName() + "," + category.getGoal());
                    first = false;
                }else{
                    writer.write("," + category.getName() + "," + category.getGoal());
                }
            }
            writer.newLine();
            for (Entry entry : entries) {
                writer.write(entry.getCategory() + "," + entry.getRetailer() + "," + entry.getDate() + "," + entry.getSpent() + "," + entry.getIsCredit());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void populateEntriesFromFile(String name) {
    }

    public void printSummarizedBudget(){
        System.out.printf("%-24s %-24s %-24s\n", "Category", "Goal", "actual");
        for (int i = 0; i < categories.size(); i++) {
            String category = "(" + i + ") " + categories.get(i).toString();
            double goal = categories.get(i).getGoal();
            double actual = getSum(categories.get(i));
            System.out.printf("%-24s %-24.2f %-24.2f\n", category, goal, actual);
        }

    }

    private double getSum(Category category) {
        double sum = 0;
        for (Entry entry : entries) {
            if (entry.getCategory().equals(category)){
                if (entry.getIsCredit()) {
                    sum += entry.getSpent();
                }else{
                    sum -= entry.getSpent();
                }
            }
        }
        return sum;
    }

    public double printItemizedListFromCategory(int index) {
        double sum = 0;
        for (Entry entry : entries) {
            if (entry.getCategory().equals(categories.get(index))) {
                entry.printEntry();
                if (entry.getIsCredit()) {
                    sum += entry.getSpent();
                } else sum -= entry.getSpent();
            }
        }
        System.out.printf("%-72s %-32s\n","","_______________");
        System.out.printf("%-81s %-36s\n","", sum);
        return sum;
    }

    public void printItemizedList() {
        double sum = 0 ;
        for (int i = 0; i < categories.size(); i++) {
            sum += printItemizedListFromCategory(i);

        }
        System.out.printf("%-72s %-32s\n","","_______________");
        System.out.printf("%-81s %-32s\n","",sum);
        System.out.printf("%-72s %-32s\n","","_______________");
    }
}
