
package UserAuthentication;
import main.mainCode;
import config.config;
import admin.manageUsers;
import static config.config.viewUsers;
import java.util.List;
import java.util.Map;

public class Authentication {
    public static int loggedInUserId;
    public static String loggedInFullName;
    public static String loggedInRole;

    public void addLogin() {
        System.out.println("\nLOGIN");
        System.out.print("Enter Email: ");
        String em = mainCode.inp.nextLine();

        System.out.print("Enter Password: ");
        String ps = mainCode.inp.nextLine();

            config con = new config();
        String qry = "SELECT * FROM tbl_users WHERE u_email = ? AND u_pass = ?";
        List<Map<String, Object>> result = con.fetchRecords(qry, em, ps);

            if (result.isEmpty()) {
                System.out.println("INVALID CREDENTIALS\n");
                return; // ✅ directly return instead of looping
            }

        Map<String, Object> users = result.get(0);

        Object statusObj = users.get("u_status");
        Object roleObj = users.get("u_role");

            if (statusObj == null || roleObj == null) {
                System.out.println("Error: Missing 'u_status' or 'u_role' column in database result.");
                return;
            }

        String stat = statusObj.toString();
        String role = roleObj.toString();

    // ✅ Check if account is approved
            if (!stat.equalsIgnoreCase("Approved")) {
                System.out.println("Your account is still pending approval. Please wait for an admin to approve it.\n");
                return;
            }

    loggedInUserId = Integer.parseInt(users.get("u_id").toString());
    loggedInFullName = users.get("u_name").toString();
    loggedInRole = role;

        System.out.println("LOGIN SUCCESS!");
        System.out.println("Welcome, " + loggedInFullName + " (" + loggedInRole + ")\n");

    // ✅ Role-based dashboard redirection
            if (role.equalsIgnoreCase("Admin")) {
                adminDashboard(con);
            } else if (role.equalsIgnoreCase("Trainer")) {
                trainerDashboard(con);
            } else if (role.equalsIgnoreCase("Staff")) {
                staffDashboard(con);
            } else {
             System.out.println("Unknown role: " + role);
            }
}
    
       public void register() {
        System.out.println("\nREGISTER ACCOUNT!");
        System.out.print("Enter Name: ");
        String nm = mainCode.inp.nextLine();

        System.out.print("Choose Role (1. Admin, 2. Trainer, 3. Staff): ");
        int chooseRole = 0;
        while (true) {
            if (!mainCode.inp.hasNextInt()) {
                System.out.print("Invalid input. Enter 1 for Admin, 2 for Trainer or 3 for Staff: ");
                mainCode.inp.nextLine();
                continue;
            }
            chooseRole = mainCode.inp.nextInt();
            mainCode.inp.nextLine();
            if (chooseRole >= 1 && chooseRole <= 3) break;
            System.out.print("Invalid choice. Enter 1 for Admin or 2 for Trainer: ");
        }

        String rl;
        switch (chooseRole) {
            case 1:
                rl = "Admin";
                break;
            case 2:
                rl = "Trainer";
                break;
            case 3:
                rl = "Staff";
                break;
            default:
                rl = "Unknown";
        }
        System.out.println("Added Successfully role " + rl + "!");

        System.out.print("Enter Email: ");
        String em = mainCode.inp.nextLine();

        config con = new config();

        // ✅ check for duplicate email
        while (true) {
            String qry = "SELECT * FROM tbl_users WHERE u_email = ?";
            List<Map<String, Object>> result = con.fetchRecords(qry, em);
            if (result.isEmpty()) break;
            System.out.print("Email already exists, Enter another Email: ");
            em = mainCode.inp.nextLine();
        }

        System.out.print("Create Password: ");
        String ps = mainCode.inp.nextLine();

        String sql = "INSERT INTO tbl_users (u_name, u_role, u_email, u_pass, u_status) VALUES (?, ?, ?, ?, ?)";
        con.addRecord(sql, nm, rl, em, ps, "Pending");

        System.out.println("\nRegistration successful! Please wait for admin approval.\n");
    }

    public void adminDashboard(config con) {
        while (true) {
            System.out.println("=== ADMIN DASHBOARD ===");
            System.out.println("1. Approve User");
            System.out.println("2. Manage Users");
            System.out.println("3. Manage Services");
            System.out.println("4. Manage Members");
            System.out.println("5. Reports");
            System.out.println("6. Logout\n");
            System.out.print("Choose an option: ");

            if (!mainCode.inp.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.\n");
                mainCode.inp.nextLine();
                continue;
            }

            int respo = mainCode.inp.nextInt();
            mainCode.inp.nextLine(); // consume newline

            switch (respo) {
                case 1:
                    viewUsers();
                    System.out.print("Enter ID to Approve: ");
                    if (!mainCode.inp.hasNextInt()) {
                        System.out.println("Invalid ID.\n");
                        mainCode.inp.nextLine();
                        continue;
                    }
                    int ids = mainCode.inp.nextInt();
                    mainCode.inp.nextLine();

                    String sql = "UPDATE tbl_users SET u_status = ? WHERE u_id = ?";
                    con.updateRecord(sql, "Approved", ids);
                    System.out.println("User with ID " + ids + " has been approved!\n");
                    break;
                case 2:
                    manageUsers mu = new manageUsers();
                    mu.manageUsers(con);
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    System.out.println("Logging out...\n");
                    mainCode.mainMenu();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.\n");
            }
        }
    }
    
    public void trainerDashboard(config con) {
        while (true) {
        System.out.println("=== TRAINER DASHBOARD ===");
        System.out.println("1. View Assigned Members");
        System.out.println("2. Check Member's Progress");
        System.out.println("3. View Schedules");        
        System.out.println("4. Logout\n");
        System.out.print("Choose an option: ");

        if (!mainCode.inp.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.\n");
            mainCode.inp.nextLine();
            continue;
        }

        int choice = mainCode.inp.nextInt();
        mainCode.inp.nextLine();

        switch (choice) {
            case 1:
                //viewApprovedMembers(con);
                break;
            case 2:
                System.out.println("Logging out...\n");
                mainCode.mainMenu(); // ✅ return to main login/register
                return;
            default:
                System.out.println("Invalid choice. Try again.\n");
            }
        }
    }

    public static void staffDashboard(config con) {
    int choice;
    while (true) {
        System.out.println("=== STAFF DASHBOARD ===");
        System.out.println("Welcome, " + Authentication.loggedInFullName + "!");
        System.out.println("1. Register Member");
        System.out.println("2. View Members List");
        System.out.println("3. View Assigned Members");
        System.out.println("3. View Membership Plans");
        System.out.println("3. View Membership Status");
        System.out.println("4. Logout");
        System.out.print("Enter choice: ");

        if (!mainCode.inp.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number.");
            mainCode.inp.nextLine();
            continue;
        }
        choice = mainCode.inp.nextInt();
        mainCode.inp.nextLine();

        switch (choice) {
            case 1:
                //viewAssignedTasks();
                break;
            case 2:
                //updateTaskStatus();
                break;
            case 3:
                //viewMembersList();
                break;
            case 4:
                System.out.println("Logging out...");
                mainCode.mainMenu();
                return; // Exit dashboard
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}

   





}
