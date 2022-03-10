import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Budget {
    private final File GOALSFILE = new File("goals.csv");
    private final File RETAILFILE = new File("retailerLink.csv");
    public ArrayList<Category> categories;
    Scanner scanner;
    public ArrayList<Entry> entries;
    public boolean csvUploade = false;

    Budget(){
        entries = new ArrayList<>();
        categories = new ArrayList<>();
        populateGoals();
        populateRail();
    }

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

    public void printBudget() {
        System.out.printf("%-24s %-24s\n","Category","Goal");
        for (int i = 0; i < categories.size(); i++) {
            String category ="(" + i + ") " + categories.get(i).toString();
            double goal = categories.get(i).goal;
            System.out.printf("%-24s %-24s\n",category,goal);
        }
    }

    public void editCategoryItem(int index, double goal) {
        Category c = categories.get(index);
        c.goal = goal;
        try(FileWriter fw = new FileWriter(GOALSFILE);
            BufferedWriter bw = new BufferedWriter(fw)){
            for (int i = 0; i < categories.size(); i++) {
                bw.write(categories.get(i) + "," + categories.get(i).goal);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadCSV(String name){
        if (categories.isEmpty()) {
            System.out.println("Empty CategoryList, Fill Categories before adding uploading data.");
            return;
        }
        String date = "";
        String retailer = "";
        String credit = "";
        String debit = "";
        String balance = "";
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
        csvUploade = true;
        double[] sums = new double[categories.size()];
        for (Entry e : entries) {
            int catNum = 0;
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

    public void printActualBudget() {
        System.out.printf("%-24s %-24s %-24s\n","Category","Goal","actual");
        for (int i = 0; i < categories.size(); i++) {
            String category ="(" + i + ") " + categories.get(i).toString();
            double goal = categories.get(i).goal;
            double actual = categories.get(i).actual;
            System.out.printf("%-24s %-24.2f %-24.2f\n",category,goal,actual);
        }
    }

    private int addToCategory(Entry e) {
        System.out.println(e.name + " not found in category type number to add to a category");
        String testZon = e.name.toLowerCase();
        if(testZon.contains("amazon") || testZon.contains("amzn")){
            categories.get(1).retailers.add(e.name);
            System.out.println("AutoWriting Amazon");
            writeRetailFile();
            return 1;
        }
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

    public void left(){
        if (!csvUploade) {
            System.out.println("Please upload CSV file first");
        }
        else{

        }
    }

}
