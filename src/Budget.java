/**
 * Primary class that handles the budget and interacts with the user interface.
 *
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Budget {
    private final File GOALSFILE = new File("goals.csv");
    private final File RETAILFILE = new File("retailerLink.csv");
    private final File ROLLOVERGOALS = new File("rollovergoals.csv");
    public ArrayList<Category> categories;
    Scanner scanner;
    public ArrayList<Entry> entries;
    public boolean csvUploaded = false;
    /**
     * Reads the goals.csv file to get budget information
     * Reads the retailerLink.csv file to get the links between retailers and categories
     */
    Budget(){
        entries = new ArrayList<>();
        categories = new ArrayList<>();
        populateGoals();
        populateRail();
    }

    /**
     * Reads the retail file and populates the Category Objects with known retailers.
     */
    private void populateRail() {
        try {
            scanner = new Scanner(RETAILFILE);
        } catch (FileNotFoundException e) {
            System.out.println("Should never happen");
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] split = line.split(",");
            String retailer = split[0];
            String category = split[1];
            for (Category c : categories) {
                if (c.name.equals(category)) {
                    c.retailers.add(retailer);
                }
            }
        }
    }

    /**
     * Reads the goals.csv file and adds on budget goals into each category object
     */

    private void populateGoals() {
        try {
            scanner = new Scanner(GOALSFILE);
        } catch (FileNotFoundException e) {
            System.out.println("Should never happen");
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] split = line.split(",");
            Category c = new Category(split[0], Double.parseDouble(split[1]));
            categories.add(c);
        }
        categories.add(new Category("Exceptions", 0));
    }

    /**
     * Prints the budget in the form of Category      Goal
     */
    public void printBudget() {
        System.out.printf("%-24s %-24s\n","Category","Goal");
        for (int i = 0; i < categories.size(); i++) {
            String category ="(" + i + ") " + categories.get(i).toString();
            double goal = categories.get(i).goal;
            System.out.printf("%-24s %-24.2f\n",category,goal);
        }
    }

    /**
     * This edits the categories budget goal.  It then writes to the goals.csv file the changes.
     * @param index index of category to be changed.
     * @param goal amount each month budget should intake
     */
    public void editCategoryItem(int index, double goal, boolean rollover) {
        File file = null;
        if(rollover){
            file = ROLLOVERGOALS;
        }
        else{
            file = GOALSFILE;
        }
        Category c = categories.get(index);
        c.goal = goal;
        try(FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw)){
            for (Category category : categories) {
                if(!category.name.equals("Exceptions")){
                    bw.write(category + "," + category.goal);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uploads a csv file in the form:
     * date,retailer name,credit,debit,account balance
     * It first checks if the retailer is already known.  If it is not then it allows user to select which category it belongs.
     * Once all the retailers are known it adds up each category and prints the actual budget method.
     * @param name of file to be uploaded.  Must be csv in form date,retailer name,credit,debit,balance
     */
    public void uploadCSV(String name){
        if (categories.isEmpty()) {
            System.out.println("Empty CategoryList, Fill Categories before adding uploading data.");
            return;
        }
        String date;
        String retailer;
        String credit;
        String debit;
        String balance;
        File file = new File(name);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] split = line.split(",");
                date = split[0];
                retailer = split[1];
                credit = split[2];
                debit = split[3];
                balance = split[4];
                entries.add(new Entry(date, retailer, credit, debit, balance,categories));
                date = "";
                retailer = "";
                credit = "";
                debit = "";
                balance = "";
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        csvUploaded = true;
        double[] sums = new double[categories.size()];
        for (Entry e : entries) {
            int catNum;
            if(!e.exists){
                catNum = addToCategory(e);
            }
            else{
                catNum = e.catNum;
            }
            double total = sums[catNum];
            if (e.credit.length() >= 1) {
                total += Double.parseDouble(e.credit);
            } else if (e.debit.length() >= 1) {
                total -= Double.parseDouble(e.debit);
            }
            sums[catNum] = total;
        }
        for (int i = 0; i < sums.length; i++) {
            categories.get(i).actual += sums[i];
        }
        printActualBudget();
    }

    /**
     * Prints the acutally used budget in the form:
     * Category      Goal     Actual
     */
    public void printActualBudget() {
        System.out.printf("%-24s %-24s %-24s\n","Category","Goal","actual");
        for (int i = 0; i < categories.size(); i++) {
            String category ="(" + i + ") " + categories.get(i).toString();
            double goal = categories.get(i).goal;
            double actual = categories.get(i).actual;
            System.out.printf("%-24s %-24.2f %-24.2f\n",category,goal,actual);
        }
    }

    /**
     * Called in upload method if the retailer does not have an associated category.
     * Amazon appears to change the numbers asspciated with them so I put in an amazon shortcut.
     * This would need to be changed as it automatically adds it to category (1)
     * @param e Entry that has the information gathered from the  csv file.
     * @return returns an integer of the category the retailer was assigned to.
     */
    private int addToCategory(Entry e) {
        for (Category c : categories) {
            for (String name : c.retailers) {
                if (name.equals(e.name)) {
                    return categories.indexOf(c);
                }
            }
        }
        String testZon = e.name.toLowerCase();
        if(testZon.contains("amazon") || testZon.contains("amzn")){
            categories.get(1).retailers.add(e.name);
            writeRetailFile();
            return 1;
        }
        System.out.println(e.name + " not found in category type number to add to a category");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println("(" + i + ") " + categories.get(i).name);
        }
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        input.nextLine();
        categories.get(choice).retailers.add(e.name);
        writeRetailFile();
        return choice;
    }

    /**
     * Writes the retailers and their respective categories to file.
     * retailer,category
     */
    private void writeRetailFile(){
        try(FileWriter fw = new FileWriter(RETAILFILE);
            BufferedWriter bw = new BufferedWriter(fw)){
            for(Category c : categories){
                for (String s : c.retailers) {
                    bw.write(s + "," + c.name);
                    bw.newLine();
                }
            }
        } catch (IOException ex) {
            System.out.println("Should never happen");
        }
    }

    /**
     * Prints out how much is left in each category.
     */
    public void left(){
        if (!csvUploaded) {
            System.out.println("Please upload CSV file first");
        }
        else{
            for (Category c : categories) {
                System.out.printf("%-24s %-24.2f\n", c.name,c.goal - c.actual);
            }
        }
    }

    /**
     * Edits the budget for how much was left or gone over from the actual.
     * Example.  If budget was $500.  You spend $450.  The budget will be rewritten to $550 for the following month.
     */
    public void rollOver() {
        for (Category c : categories) {
            c.goal = c.goal + (c.goal - c.actual);
        }
        for (int i = 0; i < categories.size(); i++) {
            editCategoryItem(i,categories.get(i).goal,true);
        }


    }


    public void saveMonth(String month) {
        File file = new File(month);
        double totalGoal = 0;
        double totalActual = 0;
        try(FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw)){
            for (Category c : categories) {
                double left = c.goal - c.actual;
                totalGoal += c.goal;
                totalActual += c.actual;
                bw.write(c.name + "," + left);
                bw.newLine();
            }
            bw.write("Total Goal " + totalGoal + "\tTotal Actual: " + totalActual + "\tOver/Under: " + (totalGoal - totalActual));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
