/**
 * QuickBudget currently is a console based program to track your purchases over a month from a single source such as a credit card.
 * You create categories and add your goal for the month.
 * Then you upload your csv file from your bank that tracks purchases and it will determine how much you have spent
 * in each category along with how much is left.
 * The CSV format from my bank I am basing this off is date,retailer,credit,debit,accountTotal.
 * Ex. 12/16/2021,Amazon.ca*2U8Z01XE0,17.01,,10421.71
 * @author Paul Zaremba
 * @version 1.0
 */


import java.util.Scanner;

public class QuickBudget {

    public static void printMenu() {
        System.out.println("Credit Card Budget options:\n(pb) print budget\n(eb [item] [value]) edit budget\n(ucsv [filename.csv]) upload csvfile\n(ab) acutal budget\n(left) amount remaining\n(m) print menu\n(ro) roll over\n(x) exits");
    }
    public static void main(String[] args) {
        Budget budget = new Budget();
        Scanner userInput = new Scanner(System.in);
        printMenu();
        String input = "";
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
                    budget.editCategoryItem(item,value);
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
                default:
                    System.out.println(input);
            }
        }
    }
}
