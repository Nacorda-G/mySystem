
package UserAuthentication;
import main.mainCode;
import config.config;
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
            System.out.println("Fetched user record: " + users); // debug

            Object statusObj = users.get("u_status");
            Object roleObj = users.get("u_role");

                if (statusObj == null || roleObj == null) {
                    System.out.println("Error: Missing 'u_status' or 'u_role' column in database result.");
                    return;
                }

            String stat = statusObj.toString();
            String role = roleObj.toString();


        loggedInUserId = Integer.parseInt(users.get("u_id").toString());
        loggedInFullName = users.get("u_name").toString();
        loggedInRole = role;

        System.out.println("LOGIN SUCCESS!");
        System.out.println("Welcome, " + loggedInFullName + " (" + loggedInRole + ")\n");

        if (role.equalsIgnoreCase("Admin")) {
            con.adminDashboard(con);
        } else if (role.equalsIgnoreCase("Trainer")) {
            trainerDashboard();
        } else {
            System.out.println("Unknown role: " + role);
        }
    }
    
       public void register() {
        System.out.println("\nREGISTER ACCOUNT!");
        System.out.print("Enter Name: ");
        String nm = mainCode.inp.nextLine();

        System.out.print("Choose Role (1. Admin, 2. Trainer): ");
        int chooseRole = 0;
        while (true) {
            if (!mainCode.inp.hasNextInt()) {
                System.out.print("Invalid input. Enter 1 for Admin or 2 for Trainer: ");
                mainCode.inp.nextLine();
                continue;
            }
            chooseRole = mainCode.inp.nextInt();
            mainCode.inp.nextLine();
            if (chooseRole == 1 || chooseRole == 2) break;
            System.out.print("Invalid choice. Enter 1 for Admin or 2 for Trainer: ");
        }

        String rl = (chooseRole == 1) ? "Admin" : "Trainer";
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

    private void adminDashboard(config con) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void trainerDashboard() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
