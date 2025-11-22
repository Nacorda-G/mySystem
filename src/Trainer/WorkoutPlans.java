
package Trainer;

import UserAuthentication.Authentication;
import static UserAuthentication.Authentication.loggedInUserId;
import static UserAuthentication.Authentication.wrapText;
import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.mainCode;


public class WorkoutPlans {
    
    public void viewAssignedMembers(config con) {
    System.out.println("\n=== MEMBERS ASSIGNED TO YOU ===");

    String sql = "SELECT m.Member_id, m.full_name, p.m_name " +
                 "FROM tbl_members m " +
                 "JOIN tbl_membershipPlan p ON m.m_id = p.m_id " +
                 "WHERE p.requires_trainer = 1 AND m.trainer_id = ?";

    try (Connection conn = con.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, loggedInUserId);

        ResultSet rs = pstmt.executeQuery();

        System.out.printf("%-10s %-25s %-20s\n", "ID", "Name", "Membership Plan");
        System.out.println("--------------------------------------------------------");

        boolean found = false;

        while (rs.next()) {
            found = true;
            System.out.printf("%-10d %-25s %-20s\n",
                              rs.getInt("Member_id"),
                              rs.getString("full_name"),
                              rs.getString("m_name"));
        }

        if (!found) {
            System.out.println("No members assigned to you that require a trainer.\n");
        }

        } catch (SQLException e) {
            System.out.println("Error fetching member details: " + e.getMessage());
        }
    }
    
    
     public void manageWorkoutPlans(config con) {
        while (true) {
            System.out.println("\n=== MANAGE MEMBER WORKOUT PLANS ===");

            viewAssignedMembers(con);
        
            System.out.print("\nEnter the **Member ID** to manage plans for (or '0' to go back): ");
        
            int selectedMemberID;
            if (mainCode.inp.hasNextInt()) {
                selectedMemberID = mainCode.inp.nextInt();
                mainCode.inp.nextLine(); 
            } else {
                mainCode.inp.nextLine();
                System.out.println("Invalid input. Returning to dashboard.");
                return;
            }

            if (selectedMemberID == 0) {
                return; // Go back to trainerDashboard
            }
        
            while (true) {
                System.out.println("\n--- Actions for Member ID: " + selectedMemberID + " ---");
                System.out.println("1. View/Select Existing Plan");
                System.out.println("2. Create Workout Plan");
                System.out.println("3. Delete Workout Plan");               
                System.out.println("4. Go back to Member Selection");
                System.out.println("5. Return to Trainer Dashboard");
                System.out.print("Choose an option: ");

                if (!mainCode.inp.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number.");
                    mainCode.inp.nextLine();
                    continue;
                }

            int planChoice = mainCode.inp.nextInt();
            mainCode.inp.nextLine();

            switch (planChoice) {
                case 1:
                    viewExistingPlans(con, selectedMemberID);
                    break;
                case 2:
                    createNewPlan(con, selectedMemberID);
                    break;
                case 3:
                    deleteWorkoutPlan(con, selectedMemberID);
                    break; 
                case 4:
                    System.out.println("Returning to Member Selection...\n");
                    break;
                case 5:
                    System.out.println("Returning to Trainer Dashboard...\n");
                    Authentication aut = new Authentication();
                    aut.trainerDashboard(con);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
            if (planChoice == 3) {
                break; // Exit the inner loop to re-select a member
            }
        }
    }
}

public void viewExistingPlans(config con, int memberID) {
    System.out.println("\n=== EXISTING WORKOUT PLANS FOR MEMBER ID: " + memberID + " ===");

    String sql = "SELECT plan_id, plan_name, exercises, start_date, end_date, created_at " +
                 "FROM tbl_workoutPlans WHERE member_id = ?";

    try (PreparedStatement pstmt = con.connectDB().prepareStatement(sql)) {
        pstmt.setInt(1, memberID);
        ResultSet rs = pstmt.executeQuery();

        boolean hasPlans = false;
        System.out.printf("%-5s %-25s %-40s %-12s %-12s %-20s\n", "ID", "Plan Name", "Exercises", "Start Date", "End Date", "Created At");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        while (rs.next()) {
            hasPlans = true;
            int planId = rs.getInt("plan_id");
            String planName = rs.getString("plan_name");
            String exercises = rs.getString("exercises");
            String startDate = rs.getString("start_date");
            String endDate = rs.getString("end_date");
            String createdAt = rs.getString("created_at");

            String wrappedExercises = wrapText(exercises, 40);
            String[] exerciseLines = wrappedExercises.split("\n");

            for (int i = 0; i < exerciseLines.length; i++) {
                if (i == 0) {
                    System.out.printf("%-5d %-25s %-40s %-12s %-12s %-20s\n",
                            planId, planName, exerciseLines[i], startDate, endDate, createdAt);
                } else {
                    System.out.printf("%-5s %-25s %-40s %-12s %-12s %-20s\n", "", "", exerciseLines[i], "", "", "");
                }
            }
        }

        if (!hasPlans) {
            System.out.println("No workout plans found for this member.");
        }

    } catch (SQLException e) {
        System.out.println("Error fetching workout plans: " + e.getMessage());
    }
}



public void createNewPlan(config con, int memberID) {
    System.out.println("\n=== CREATE NEW WORKOUT PLAN ===");

    System.out.print("Enter plan name: ");
    String planName = mainCode.inp.nextLine();

    System.out.print("Enter exercises (comma separated or details): ");
    String exercises = mainCode.inp.nextLine();

    System.out.print("Enter start date (YYYY-MM-DD): ");
    String startDate = mainCode.inp.nextLine();

    System.out.print("Enter end date (YYYY-MM-DD): ");
    String endDate = mainCode.inp.nextLine();

    String sql = "INSERT INTO tbl_workoutPlans(member_id, plan_name, exercises, start_date, end_date, created_at) " +
                 "VALUES (?, ?, ?, ?, ?, datetime('now'))";

    try (PreparedStatement pstmt = con.connectDB().prepareStatement(sql)) {
        pstmt.setInt(1, memberID);
        pstmt.setString(2, planName);
        pstmt.setString(3, exercises);
        pstmt.setString(4, startDate);
        pstmt.setString(5, endDate);

        int rows = pstmt.executeUpdate();
        if (rows > 0) {
            System.out.println("✔ Workout plan created successfully!");

            //Send email to the member
            String query = "SELECT full_name, member_email FROM tbl_members WHERE member_id = ?";
            try (PreparedStatement ps = con.connectDB().prepareStatement(query)) {
                ps.setInt(1, memberID);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String fullName = rs.getString("full_name");
                    String email = rs.getString("member_email");

                    helper.EmailHelper.sendWorkoutPlanEmail(
                        email,
                        fullName,
                        planName,
                        exercises,
                        startDate,
                        endDate
                    );
                }
            } catch (SQLException ex) {
                System.out.println("❌ Failed to fetch member info for email: " + ex.getMessage());
            }

        } else {
            System.out.println("❌ Failed to create workout plan.");
        }

    } catch (SQLException e) {
        System.out.println("Error creating workout plan: " + e.getMessage());
    }
}

public void deleteWorkoutPlan(config con, int memberID) {
    String query = "SELECT plan_id, plan_name FROM tbl_WorkoutPlans WHERE member_id = ?";
    
    try (Connection conn = con.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setInt(1, memberID);
        ResultSet rs = pstmt.executeQuery();

        System.out.println("\n=== EXISTING WORKOUT PLANS FOR MEMBER ID: " + memberID + " ===");
        boolean hasPlans = false;
        while (rs.next()) {
            hasPlans = true;
            System.out.println("Plan ID: " + rs.getInt("plan_id") + 
                              " | Plan Name: " + rs.getString("plan_name"));
        }
        if (!hasPlans) {
            System.out.println("No workout plans found for this member.");
            return;
        }

        // Ask which plan to delete
        System.out.print("\nEnter the Plan ID you want to DELETE (or 0 to cancel): ");
        if (!mainCode.inp.hasNextInt()) {
            mainCode.inp.nextLine();
            System.out.println("Invalid input. Cancellation.");
            return;
        }

        int planID = mainCode.inp.nextInt();
        mainCode.inp.nextLine();

        if (planID == 0) {
            System.out.println("Deletion cancelled.");
            return;
        }

        // Confirm deletion
        System.out.print("Are you sure you want to DELETE plan ID " + planID + "? (y/n): ");
        String confirm = mainCode.inp.nextLine();

        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Deletion canceled.");
            return;
        }

        // Perform deletion
        String deleteSQL = "DELETE FROM tbl_WorkoutPlans WHERE plan_id = ? AND member_id = ?";
        try (PreparedStatement deletePstmt = conn.prepareStatement(deleteSQL)) {
            deletePstmt.setInt(1, planID);
            deletePstmt.setInt(2, memberID);

            int result = deletePstmt.executeUpdate();
            if (result > 0) {
                System.out.println("Workout plan deleted successfully.");
            } else {
                System.out.println("Failed to delete plan. It may not exist.");
            }
        }

    } catch (SQLException e) {
        System.out.println("Error deleting workout plan: " + e.getMessage());
    }
}


}
