import java.util.Scanner;

public class QuickBudget {

    public static void printMenu() {
        System.out.println("Credit Card Budget options:\n(pb) print budget\n(eb [item] [value]) edit budget\n(ucsv [filename.csv]) upload csvfile\n(ab) acutal budget\n(left) amount remaining\n(m) print menu\n(x) exits");
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
                default:
                    System.out.println(input);
            }
        }
    }
}
