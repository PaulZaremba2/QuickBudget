package com.zaremba.quickbudget;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Budget {
    private ArrayList<Category> categories;
    private final File CATEGORIESLIST = new File("categories.csv");
    public Budget(){
        categories = new ArrayList<>();
        try {
            Scanner reader = new Scanner(CATEGORIESLIST);
            while (reader.hasNext()) {
                String line = reader.nextLine();
                String[] splitter = line.split(",");
                String catName = splitter[0];
                double goal = Double.parseDouble(splitter[1]);
                categories.add(new Category(catName, goal));
            }
        } catch (FileNotFoundException e) {
            System.err.println("File: " + CATEGORIESLIST.getName() + " Not found: System exiting");
            System.exit(0);
        }
    }

    public void printBudget() {
        System.out.printf("%-24s %-24s\n","Category","Goal");
        for (int i = 0; i < categories.size(); i++) {
            String category ="(" + i + ") " + categories.get(i).getName();
            double goal = categories.get(i).getGoal();
            System.out.printf("%-24s %-24.2f\n",category,goal);
        }
    }

    public void addMonth(String yearMonth, String fileName) {
        File file = new File(fileName);
        ArrayList<Entry> entries = new ArrayList<>();
        try{
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] splitter = line.split(",");
                String date = splitter[0];
                String retailerName = splitter[1];
                String debit = splitter[2];
                String credit = splitter[2];
                Category category = getRetailerCategory(retailerName);
                Retailer retailer = new Retailer(retailerName, category);
                Entry entry = null;
                if (debit.length() > 0) {
                    entry = new Entry(category, retailer, date, Double.parseDouble(debit), false);
                }
                if (credit.length() > 0) {
                    entry = new Entry(category, retailer, date, Double.parseDouble(credit), true);
                }
                entries.add(entry);
            }
            Month month = new Month(yearMonth, entries, categories);
            month.printSummarizedBudget();
            Scanner userChoice = new Scanner(System.in);
            System.out.println("See itemized list of Category by typing index position or [all] for all items sorted by category [x] back to main menu");
            while(true){
                String choice = userChoice.nextLine();
                if (choice.equals("all")) {
                    month.printItemizedList();
                } else if (choice.equals("x")) {
                    break;
                } else {
                    try {
                        int index = Integer.parseInt(choice);
                        if (index < categories.size() && index >= 0) {
                            month.printItemizedListFromCategory(index);
                        }
                        else{
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid choice");
                    }
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Category getRetailerCategory(String name) {
        for (Category c : categories) {
            try {
                Scanner scanner = new Scanner(new File("Categories/" + c.getName() +".csv"));
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.equals(name)) {
                        return c;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Choose Category from list to add " + name + ":");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println("(" + i + ")\t " + categories.get(i).getName());
        }
        Scanner scanner = new Scanner(System.in);
        int index = Integer.parseInt(scanner.nextLine());
        try {
            FileWriter writer = new FileWriter("Categories/" + categories.get(index).getName() + ".csv",true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(name);
            bw.newLine();
            bw.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categories.get(index);
    }

    public void listMonths() {
    }

    public void display(int index) {
    }

    public void showCurrentMonth(String next) {
    }
}
