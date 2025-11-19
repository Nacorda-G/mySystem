
package admin;

import config.config;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import main.mainCode;
import java.sql.SQLException;

public class manageMembers {

    private static final config con = new config(); 
    private static final java.util.Scanner sc = mainCode.inp;

    public static void memberMenu() {
        int choice;
        do {
            System.out.println("\n=== MANAGE MEMBERS ===");
            System.out.println("1. View Members");
            System.out.println("2. Update Member");
            System.out.println("3. Delete Member");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    viewMembers();
                    break;
                case 2:
                    viewMembers();
                    updateMember();
                    break;
                case 3:
                    viewMembers();
                    deleteMember();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 4);
    }

    private static void viewMembers() {
        System.out.println("\n=== MEMBERS LIST ===");
        String sql = "SELECT m.member_id, m.full_name, m.member_age,\n" +
                     "p.m_name AS plan_name\n" +
                     "FROM tbl_members m\n" +
                     "LEFT JOIN tbl_membershipPlan p ON m.m_id = p.m_id";

        try (Connection conn = con.connectDB();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

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

        } catch (SQLException e) {
            System.out.println("❌ Error viewing members list: " + e.getMessage());
        }
    }

    private static void updateMember() {
        System.out.print("Enter Member ID to update: ");
        int memberId = Integer.parseInt(sc.nextLine());

        System.out.print("Enter new full name (leave blank to keep current): ");
        String fullName = sc.nextLine();

        System.out.print("Enter new age (leave blank to keep current): ");
        String ageInput = sc.nextLine();
        
        manageMembership mm = new manageMembership();
        mm.viewPlans();
        System.out.print("Enter new membership plan ID (leave blank to keep current): ");
        String planIdInput = sc.nextLine();

        try (Connection conn = con.connectDB()) {
            
            StringBuilder sql = new StringBuilder("UPDATE tbl_members SET ");
            boolean first = true;

            if (!fullName.isEmpty()) {
                sql.append("full_name = ?");
                first = false;
            }
            if (!ageInput.isEmpty()) {
                if (!first) sql.append(", ");
                sql.append("member_age = ?");
                first = false;
            }
            if (!planIdInput.isEmpty()) {
                if (!first) sql.append(", ");
                sql.append("m_id = ?");
            }

            sql.append(" WHERE member_id = ?");

            try (PreparedStatement pst = conn.prepareStatement(sql.toString())) {
                int index = 1;
                if (!fullName.isEmpty()) pst.setString(index++, fullName);
                if (!ageInput.isEmpty()) pst.setInt(index++, Integer.parseInt(ageInput));
                if (!planIdInput.isEmpty()) pst.setInt(index++, Integer.parseInt(planIdInput));
                pst.setInt(index, memberId);

                int updated = pst.executeUpdate();
                if (updated > 0) {
                    System.out.println("✅ Member updated successfully!");
                } else {
                    System.out.println("❌ Member ID not found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error updating member: " + e.getMessage());
        }
    }

    private static void deleteMember() {
        System.out.print("Enter Member ID to delete: ");
        int memberId = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM tbl_members WHERE member_id = ?";

        try (Connection conn = con.connectDB();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, memberId);
            int deleted = pst.executeUpdate();
            if (deleted > 0) {
                System.out.println("✅ Member deleted successfully!");
            } else {
                System.out.println("❌ Member ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error deleting member: " + e.getMessage());
        }
    }

}

