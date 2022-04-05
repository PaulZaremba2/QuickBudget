/**
 * QuickBudget currently is a console based program to track your purchases over a month from a single source such as a credit card.
 * You create categories and add your goal for the month.
 * Then you upload your csv file from your bank that tracks purchases and it will determine how much you have spent
 * in each category along with how much is left.
 * The CSV format from my bank I am basing this off is date,retailer,credit,debit,accountTotal.
 * Ex. 12/16/2021,Amazon.ca*2U8Z01XE0,17.01,,10421.71
 * It can take in two command line arguments the first one can be month the second is the csv file.  The program will do a quick print out
 * of your actual budget assuming all retailers are known.  Otherwise run the program with no commandline arguments to add categories to
 * the unknown retailers.  The first argument of the month is for future application of recording budgets month by month.
 * @author Paul Zaremba
 * @version 1.0
 */

import java.util.Scanner;

public class QuickBudget {

    public static void printMenu() {
        System.out.println("Credit Card Budget options:\n(pb) print budget\n(eb [item] [value]) edit budget\n(ucsv [filename.csv]) upload csvfile\n(ab) acutal budget\n(left) amount remaining\n(m) print menu\n(ro) roll over\n(save [month]) Save Budget\n(x) exits");
    }
    public static void main(String[] args) {
        Budget budget = new Budget();
        Scanner userInput = new Scanner(System.in);
        String input = "";
        //TODO Make method to print out this command line argument better and more streamlined.
        if (args.length > 1) {
            String month = args[0];
            budget.uploadCSV(args[1]);
            budget.saveMonth(month);
            System.exit(0);
        }
        printMenu();
        while(true){
            input = userInput.next();
            switch(input){
                case "x":
                    System.out.println("Exiting budgeter");
                    System.exit(0);
                case "pb":
                    budget.printBudget();
                    break;
                case "eb":
                    int item = userInput.nextInt();
                    int value = userInput.nextInt();
                    userInput.nextLine();
                    budget.editCategoryItem(item,value, false);
                    budget.printBudget();
                    break;
                case "ucsv":
                    budget.uploadCSV(userInput.next());
                    break;
                case "ab":
                    budget.printActualBudget();
                    break;
                case "m":
                    printMenu();
                    break;
                case "left":
                    budget.left();
                    break;
                case "ro":
                    budget.rollOver();
                    break;
                case "save":
                    String month = userInput.next();
                    budget.saveMonth(month);
                    break;
                default:
                    System.out.println(input);
            }
        }
    }
}
