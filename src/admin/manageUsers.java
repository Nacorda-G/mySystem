
package admin;
import java.util.Scanner;
import config.config;
import static config.config.viewUsers;


public class manageUsers {
    private Scanner inp = new Scanner(System.in);

    public void manageUsers(config con) {
        while (true) {
            System.out.println("=== MANAGE USERS ===");
            System.out.println("1. View Users");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. Back to Dashboard\n");
            System.out.print("Choose an option: ");

            if (!inp.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.\n");
                inp.nextLine();
                continue;
            }

            int choice = inp.nextInt();
            inp.nextLine();

            switch (choice) {
                case 1:
                    viewUsers();
                    break;
                case 2:
                    viewUsers();
                    updateUser();
                    break;
                case 3:
                    viewUsers();
                    deleteUser();
                    break;
                case 4:
                    System.out.println("Returning to Admin Dashboard...\n");
                    return;
                default:
                    System.out.println("Invalid option. Try again.\n");
            }
        }
    }
        
   
    public void updateUser(){
        System.out.print("\nEnter User ID to Update: ");
        if (!inp.hasNextInt()) {
            System.out.println("Invalid ID.\n");
            inp.nextLine();
            return;
        }
        int id = inp.nextInt();
        inp.nextLine();

        System.out.print("Enter New Name: ");
        String name = inp.nextLine();

        System.out.print("Enter New Email: ");
        String email = inp.nextLine();

        System.out.print("Enter New Status (Approved/Pending): ");
        String status = inp.nextLine();
        
        config con = new config();

        String sql = "UPDATE tbl_users SET u_name = ?, u_email = ?, u_status = ? WHERE u_id = ?";
        con.updateRecord(sql, name, email, status, id);

        System.out.println("✅ User ID " + id + " has been updated.\n");
    }

    
    private void deleteUser() {
        System.out.print("\nEnter User ID to Delete: ");
        if (!inp.hasNextInt()) {
            System.out.println("Invalid ID.\n");
            inp.nextLine();
            return;
        }
        int id = inp.nextInt();
        inp.nextLine();

        System.out.print("Are you sure you want to delete user with ID " + id + "? (y/n): ");
        String confirm = inp.nextLine();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("❎ Delete cancelled.\n");
            return;
        }
        
        config con = new config();

        String sql = "DELETE FROM tbl_users WHERE u_id = ?";
        con.deleteRecord(sql, id);

        System.out.println("🗑️ User with ID " + id + " has been deleted.\n");
        }
}