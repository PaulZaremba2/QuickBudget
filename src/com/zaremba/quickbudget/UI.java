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
 * @version 1.1
 */

package com.zaremba.quickbudget;

import java.util.Scanner;

public class UI {
    public static void printMenu() {
        System.out.println("Credit Card Budget options:\n(pb) print budget\n(am [year/month] [filename.csv]) upload csvfile of monthly budget\n(lm) list uploaded months\n(display [month index number])\n(cm [filename.csv] show current months budget\n(x) exits");
    }
    public static void main(String[] args) {
        Budget budget = new Budget();
        Scanner userInput = new Scanner(System.in);
        String input = "";
        //TODO Make method to print out this command line argument better and more streamlined.
        while(true){
            printMenu();
            input = userInput.next();
            switch(input){
                case "x":
                    System.out.println("Exiting budgeter");
                    System.exit(0);
                case "pb":
                    budget.printBudget();
                    break;
                case "am":
                    String yearMonth = userInput.next();
                    String fileName = userInput.next();
                    userInput.nextLine();
                    budget.addMonth(yearMonth, fileName);
                    break;
                case "lm":
                    budget.listMonths();
                    break;
                case "display":
                    int index = userInput.nextInt();
                    userInput.nextLine();
                    budget.display(index);
                    break;
                case "cm":
                    budget.showCurrentMonth(userInput.next());
                    break;
                default:
                    System.out.println(input);
            }
        }

    }
}
