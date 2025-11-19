
package UserAuthentication;
import admin.manageMembership;
import admin.manageServices;
import main.mainCode;
import config.config;
import admin.manageUsers;
import static config.config.viewUsers;
import java.sql.*;
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
            System.out.println("4. Manage Membership Plan");
            System.out.println("5. Manage Members");
            System.out.println("6. Reports");
            System.out.println("7. Logout\n");
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
                    manageServices ms = new manageServices();
                    ms.menu();
                    break;
                case 4:
                    manageMembership membership = new manageMembership();
                    membership.membershipDashboard();
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
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
        System.out.println("1.View Members List");
        System.out.println("2. Check Member's Progress");        
        System.out.println("3. Logout\n");
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
                break;
            case 2:
                break;
            case 3:
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
        System.out.println("3. View Membership Plans");        
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
                registerMember(con);
                break;
            case 2:
                viewMembersList(con);
                break;
            case 3:
                manageMembership membership = new manageMembership();
                membership.viewPlans();
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

    

    // ============================================
    // CASE 1 — REGISTER MEMBER
    // ============================================
    private static void registerMember(config con) {
    System.out.println("\n=== REGISTER MEMBER ===");
    
    // --- 1. Input Collection ---
    System.out.print("Full Name: ");
    String name = mainCode.inp.nextLine();

    System.out.print("Age: ");
    int age = mainCode.inp.nextInt(); // maps to member_age
    mainCode.inp.nextLine();
    
    manageMembership membership = new manageMembership();
    membership.viewPlans(); // Assumes this uses a separate, closed connection or doesn't leak one

    System.out.print("Membership Plan ID: ");
    int planId = mainCode.inp.nextInt(); // maps to m_id
    mainCode.inp.nextLine();
    
    Integer trainerId = null; // Will store the trainer ID if required

    // --- 2. Database Operations (Single Connection) ---
    // The main Connection is opened here and guaranteed to be closed at the end of the try block.
    try (Connection dbConnection = con.connectDB()) {
        
        // Validate Plan
        String checkPlanSQL = "SELECT requires_trainer FROM tbl_membershipPlan WHERE m_id = ?";
        
        // Use try-with-resources for PreparedStatement and ResultSet (guarantees closure)
        try (PreparedStatement checkPlan = dbConnection.prepareStatement(checkPlanSQL)) {
            checkPlan.setInt(1, planId);
            
            try (ResultSet planRS = checkPlan.executeQuery()) {
                if (!planRS.next()) {
                    System.out.println("Invalid membership plan! Registration cancelled.");
                    return;
                }
                
                boolean requiresTrainer = planRS.getBoolean("requires_trainer");
                
                // Trainer Selection Block
                if (requiresTrainer) {
                    // Show Available Trainers
                    System.out.println("\n--- Available Trainers ---");
                    String trainerListSQL = "SELECT u_id, u_name FROM tbl_users WHERE u_role = 'Trainer'";
                    
                    // Use try-with-resources for Trainer Listing
                    try (PreparedStatement listTrainers = dbConnection.prepareStatement(trainerListSQL);
                         ResultSet trainerList = listTrainers.executeQuery()) {
                        
                        while (trainerList.next()) {
                            System.out.printf("ID: %-5d Name: %s%n",
                                    trainerList.getInt("u_id"), trainerList.getString("u_name"));
                        }
                    } // listTrainers and trainerList are closed here

                    // Input Trainer ID
                    System.out.print("\nEnter Trainer ID: ");
                    trainerId = mainCode.inp.nextInt();
                    mainCode.inp.nextLine();

                    // Validate Trainer
                    String checkTrainerSQL = "SELECT * FROM tbl_users WHERE u_id = ? AND u_role = 'Trainer'";
                    
                    // Use try-with-resources for Trainer Validation
                    try (PreparedStatement checkTrainer = dbConnection.prepareStatement(checkTrainerSQL)) {
                        checkTrainer.setInt(1, trainerId);
                        
                        try (ResultSet trainerRS = checkTrainer.executeQuery()) {
                            if (!trainerRS.next()) {
                                System.out.println("Invalid Trainer ID! Registration cancelled.");
                                return;
                            }
                        } // trainerRS is closed here
                    } // checkTrainer is closed here
                    
                } else {
                    System.out.println("This membership plan does NOT require a trainer.");
                }
            } // planRS is closed here
        } // checkPlan is closed here
        
        // STEP 3: Insert Member (The main write operation)
        String sql = "INSERT INTO tbl_members (full_name, member_age, m_id, trainer_id) VALUES (?, ?, ?, ?)";
        
        // Use try-with-resources for the final INSERT statement
        try (PreparedStatement pst = dbConnection.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setInt(2, age);
            pst.setInt(3, planId);

            if (trainerId == null) {
                pst.setNull(4, java.sql.Types.INTEGER);
            } else {
                pst.setInt(4, trainerId);
            }

            pst.executeUpdate();
            System.out.println("\nMember Registered Successfully!");
        } // pst is closed here

    } catch (Exception e) {
        // This catch block handles SQL exceptions and input errors.
        System.out.println("Error registering member: " + e.getMessage());
    } 
    // dbConnection is automatically closed here, releasing the database lock.
}
    private static void viewMembersList(config con) {
    try {
        System.out.println("\n=== MEMBERS LIST ===");

        String sql = "SELECT m.member_id, m.full_name, m.member_age, " +
                     "p.m_name AS plan_name " +
                     "FROM tbl_members m " +
                     "LEFT JOIN tbl_membershipPlan p ON m.m_id = p.m_id";

        PreparedStatement pst = con.connectDB().prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        System.out.printf("%-10s %-25s %-8s %-20s%n",
                          "MemberID", "Full Name", "Age", "Plan");
        System.out.println("-----------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-10d %-25s %-8d %-20s%n",
                    rs.getInt("member_id"),
                    rs.getString("full_name"),
                    rs.getInt("member_age"),
                    rs.getString("plan_name"));
        }

    } catch (Exception e) {
        System.out.println("Error viewing members list: " + e.getMessage());
    }
}



}

   





