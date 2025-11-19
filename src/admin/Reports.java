
package admin;

import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.mainCode;

public class Reports {

    private static final config con = new config(); 

    public static void reportsMenu() {
        int choice;
        do {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1. Total Members");
            System.out.println("2. Members by Membership Plan");
            System.out.println("3. Members Assigned to Trainers");
            System.out.println("4. Back to Admin Dashboard");
            System.out.print("Choose: ");
            choice = Integer.parseInt(mainCode.inp.nextLine());

            switch (choice) {
            case 1:
                ReportTotalMembers();
                break;
            case 2:
                reportMembersByPlan();
                break;
            case 3:
                reportMembersByTrainer();
                break;
            case 4:
                System.out.println("Returning to dashboard...");
            default:System.out.println("Invalid choice!");
        }

        } while (choice != 4);
    }

    private static void ReportTotalMembers() {
        String sql = "SELECT COUNT(*) AS total FROM tbl_members";
        try (Connection conn = con.connectDB();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                System.out.println("\nTotal Members: " + rs.getInt("total"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error generating total members report: " + e.getMessage());
        }
    }

    private static void reportMembersByPlan() {
        String sql = "SELECT p.m_name AS plan, COUNT(m.member_id) AS total\n" +
                     "FROM tbl_members m\n" +
                     "JOIN tbl_membershipPlan p ON m.m_id = p.m_id\n" +
                     "GROUP BY p.m_name";

        try (Connection conn = con.connectDB();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("\nMembership Plan | Total Members");
            System.out.println("-------------------------------");

            while (rs.next()) {
                System.out.printf("%-15s | %d%n",
                        rs.getString("plan"),
                        rs.getInt("total"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error generating members by plan report: " + e.getMessage());
        }
    }

    private static void reportMembersByTrainer() {
        String sql = "SELECT t.u_name AS trainer, COUNT(m.member_id) AS total\n" +
                     "FROM tbl_members m\n" +
                     "JOIN tbl_users t ON m.trainer_id = t.u_id\n" +
                     "WHERE m.trainer_id IS NOT NULL\n" +
                     "GROUP BY t.u_name";

        try (Connection conn = con.connectDB();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("\nTrainer Name      | Total Members Assigned");
            System.out.println("-----------------------------------------");

            while (rs.next()) {
                System.out.printf("%-18s | %d%n",
                        rs.getString("trainer"),
                        rs.getInt("total"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error generating members by trainer report: " + e.getMessage());
        }
    }
}

