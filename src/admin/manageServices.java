
package admin;
import java.sql.*;
import java.util.Scanner;
import config.config;

public class manageServices {
    Scanner sc = new Scanner(System.in);
    Connection con;

    public manageServices() {
        con = config.connectDB(); // Connect to the database
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== MANAGE SERVICES ===");
            System.out.println("1. Add Service");
            System.out.println("2. View Services");
            System.out.println("3. Update Service");
            System.out.println("4. Delete Service");
            System.out.println("5. Back to Admin Dashboard");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addService();
                    break;
                case 2: 
                    viewServices();
                    break;
                case 3: 
                    viewServices();
                    updateService();
                    break;
                case 4:
                    viewServices();
                    deleteService();
                    break;
                case 5: 
                 return; 
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private void addService() {
        System.out.println("\n=== ADD SERVICE ===");
        System.out.print("Enter Service Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Description: ");
        String desc = sc.nextLine();

        String sql = "INSERT INTO tbl_services(s_name, s_desc, s_created, s_updated) VALUES (?, ? )";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, desc);
            pstmt.executeUpdate();
            System.out.println("Service added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding service: " + e.getMessage());
        }
    }

    private void viewServices() {
    System.out.println("\n=== SERVICE LIST ===");
    String sql = "SELECT * FROM tbl_services";
    try (Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        System.out.printf("%-5s %-20s %-65s\n",
                "ID", "Name", "Description");
        System.out.println("----------------------------------------------------------------------------------------------------------------------");

        while (rs.next()) {
            int id = rs.getInt("s_id");
            String name = rs.getString("s_name");
            String desc = rs.getString("s_desc");
            if (desc == null) desc = ""; // guard against null

            // Wrap the description text to fit within 65 characters
            String[] wrappedDesc = wrapText(desc, 65);

            // Print the first line with all columns
            System.out.printf("%-5d %-20s %-65s\n",
                    id, name, wrappedDesc.length > 0 ? wrappedDesc[0] : "");

            // Print remaining description lines (if any)
            for (int i = 1; i < wrappedDesc.length; i++) {
                System.out.printf("%-5s %-20s %-65s\n",
                        "", "", wrappedDesc[i]);
            }

            System.out.println("----------------------------------------------------------------------------------------------------------------------");
        }

        System.out.println("\nPress Enter to proceed...");
        sc.nextLine();

    } catch (SQLException e) {
        System.out.println("Error viewing services: " + e.getMessage());
    }
}

    private void updateService() {
        System.out.println("\n=== UPDATE SERVICE ===");
        System.out.print("Enter Service ID to Update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Service Name: ");
        String name = sc.nextLine();
        System.out.print("Enter New Description: ");
        String desc = sc.nextLine();

        String sql = "UPDATE tbl_services SET s_name=?, s_desc=?, WHERE s_id=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, desc);
            pstmt.setInt(6, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Service updated successfully!");
            } else {
                System.out.println("No service found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating service: " + e.getMessage());
        }
    }

    private void deleteService() {
        System.out.println("\n=== DELETE SERVICE ===");
        System.out.print("Enter Service ID to Delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM tbl_services WHERE s_id=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Service deleted successfully!");
            } else {
                System.out.println("No service found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting service: " + e.getMessage());
        }
    }
    
    
    private String[] wrapText(String text, int width) {
    java.util.List<String> lines = new java.util.ArrayList<>();
    if (text == null || text.isEmpty()) {
        return new String[0];
    }

    StringBuilder line = new StringBuilder();
    for (String word : text.split(" ")) {
        // If adding the next word would exceed the width, push the current line and start a new one
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