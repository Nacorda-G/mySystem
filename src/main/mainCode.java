
package main;
import UserAuthentication.Authentication;
import java.util.Scanner;

public class mainCode {
    public static Scanner inp = new Scanner(System.in);
    

    // ✅ Main menu method
    public static void mainMenu() {
        Authentication aut = new Authentication();

        while (true) {
            System.out.println("\nWELCOME TO MY SYSTEM!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit\n");

            System.out.print("Choose an option: ");

            if (!inp.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.\n");
                inp.nextLine();
                continue;
            }

            int option = inp.nextInt();
            inp.nextLine();

            switch (option) {
                case 1:
                    aut.addLogin();
                    break;
                case 2:
                    aut.register();
                    break;
                case 3:
                    System.out.println("Exiting system...");
                    return; // ✅ stop the loop and exit the program
                default:
                    System.out.println("Invalid input, Try Again.\n");
                    break;
            }
        }
    }

    // ✅ Main method (program entry point)
    public static void main(String[] args) {
        mainMenu(); // start main menu when the program runs
    }
}

