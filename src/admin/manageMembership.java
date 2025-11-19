package admin;

import config.config;
import java.sql.*;
import java.util.Scanner;

public class manageMembership {

    private Scanner sc = new Scanner(System.in);
    private Connection con;

    // Constructor – establish DB connection
    public manageMembership() {
        con = config.connectDB();
    }

    // ============================================================
    // DASHBOARD – MAIN MENU
    // ============================================================
    public void membershipDashboard() {
        while (true) {
            System.out.println("\n=== MEMBERSHIP MANAGEMENT ===");
            System.out.println("1. Add Membership Plan");
            System.out.println("2. View Plans");
            System.out.println("3. Update Plan");
            System.out.println("4. Delete Plan");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addPlan();
                    break;
                case 2:
                    viewPlans();
                    break;
                case 3:
                    viewPlans();
                    updatePlan();
                    break;
                case 4:
                    viewPlans();
                    deletePlan();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ============================================================
    // ADD MEMBERSHIP PLAN
    // ============================================================
    private void addPlan() {
        try {
            System.out.println("\n--- ADD MEMBERSHIP PLAN ---");

            System.out.print("Plan Name: ");
            String name = sc.nextLine();

            System.out.print("Service Name: ");
            String sName = sc.nextLine();

            System.out.print("Service Description: ");
            String sDesc = sc.nextLine();

            System.out.print("Duration (weeks/months): ");
            String duration = sc.nextLine();

            System.out.print("Price: ");
            double price = sc.nextDouble();
            sc.nextLine();

            System.out.print("Does this plan require a trainer? (y/n): ");
            String trainerChoice = sc.nextLine().trim().toLowerCase();
            boolean requiresTrainer = trainerChoice.equals("y");

            String sql = "INSERT INTO tbl_membershipPlan (m_name, s_name, s_desc, m_duration, m_price, requires_trainer) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, sName);
            pst.setString(3, sDesc);
            pst.setString(4, duration);
            pst.setDouble(5, price);
            pst.setBoolean(6, requiresTrainer);

            pst.executeUpdate();
            System.out.println("Membership plan added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding plan: " + e.getMessage());
        }
    }

    // ============================================================
    // VIEW MEMBERSHIP PLANS
    // ============================================================
   public void viewPlans() {
    System.out.println("\n=== MEMBERSHIP PLANS ===");
    String sql = "SELECT * FROM tbl_membershipPlan ORDER BY m_id";

    try (Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        // Table header
        System.out.printf("%-5s %-20s %-15s %-10s %-10s %-20s %-50s\n",
                "ID", "Plan Name", "Duration", "Price", "Trainer", "Service Name", "Service Description");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");

        while (rs.next()) {
            int id = rs.getInt("m_id");
            String planName = rs.getString("m_name");
            String duration = rs.getString("m_duration");
            double price = rs.getDouble("m_price");
            boolean requiresTrainer = rs.getBoolean("requires_trainer");
            String serviceName = rs.getString("s_name");
            String serviceDesc = rs.getString("s_desc");
            if (serviceDesc == null) serviceDesc = "";

            // Wrap service description to fit in 50 characters
            String[] wrappedDesc = wrapText(serviceDesc, 50);

            // Print the first line with all columns
            System.out.printf("%-5d %-20s %-15s %-10.2f %-10s %-20s %-50s\n",
                    id, planName, duration, price, requiresTrainer ? "YES" : "NO",
                    serviceName, wrappedDesc.length > 0 ? wrappedDesc[0] : "");

            // Print remaining wrapped description lines (if any)
            for (int i = 1; i < wrappedDesc.length; i++) {
                System.out.printf("%-5s %-20s %-15s %-10s %-10s %-20s %-50s\n",
                        "", "", "", "", "", "", wrappedDesc[i]);
            }

            // Optional: horizontal separator between rows
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
        }

        System.out.println("\nPress Enter to proceed...");
        sc.nextLine();

    } catch (SQLException e) {
        System.out.println("Error viewing plans: " + e.getMessage());
    }
}


    // ============================================================
    // UPDATE MEMBERSHIP PLAN
    // ============================================================
    private void updatePlan() {
        try {
            System.out.print("\nEnter Plan ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();

            // Check if plan exists
            String check = "SELECT * FROM tbl_membershipPlan WHERE m_id=?";
            PreparedStatement checkPst = con.prepareStatement(check);
            checkPst.setInt(1, id);
            ResultSet rs = checkPst.executeQuery();

            if (!rs.next()) {
                System.out.println("Plan not found!");
                return;
            }

            System.out.print("New Plan Name: ");
            String name = sc.nextLine();

            System.out.print("New Service Name: ");
            String sName = sc.nextLine();

            System.out.print("New Service Description: ");
            String sDesc = sc.nextLine();

            System.out.print("New Duration (weeks/months): ");
            String duration = sc.nextLine();

            System.out.print("New Price: ");
            double price = sc.nextDouble();
            sc.nextLine();

            System.out.print("Does this plan require a trainer? (y/n): ");
            String trainerChoice = sc.nextLine().trim().toLowerCase();
            boolean requiresTrainer = trainerChoice.equals("y");

            String update = "UPDATE tbl_membershipPlan SET m_name=?, s_name=?, s_desc=?, m_duration=?, m_price=?, requires_trainer=? "
                          + "WHERE m_id=?";
            PreparedStatement pst = con.prepareStatement(update);
            pst.setString(1, name);
            pst.setString(2, sName);
            pst.setString(3, sDesc);
            pst.setString(4, duration);
            pst.setDouble(5, price);
            pst.setBoolean(6, requiresTrainer);
            pst.setInt(7, id);

            pst.executeUpdate();
            System.out.println("Membership plan updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating plan: " + e.getMessage());
        }
    }

    // ============================================================
    // DELETE MEMBERSHIP PLAN
    // ============================================================
    private void deletePlan() {
        try {
            System.out.print("\nEnter Plan ID to delete: ");
            int id = sc.nextInt();
            sc.nextLine();

            String sql = "DELETE FROM tbl_membershipPlan WHERE m_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);

            int rows = pst.executeUpdate();
            if (rows > 0)
                System.out.println("Membership plan deleted!");
            else
                System.out.println("Plan not found!");

        } catch (SQLException e) {
            System.out.println("Error deleting plan: " + e.getMessage());
        }
    }

    // ============================================================
    // TEXT WRAP HELPER
    // ============================================================
    private String[] wrapText(String text, int width) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        if (text == null || text.isEmpty()) return new String[0];

        StringBuilder line = new StringBuilder();
        for (String word : text.split(" ")) {
            if (line.length() + word.length() + (line.length() == 0 ? 0 : 1) > width) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
        }
        if (line.length() > 0) lines.add(line.toString());

        return lines.toArray(new String[0]);
    }
}
