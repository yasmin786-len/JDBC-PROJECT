import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class hotelManagement {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/hotel"; // Your DB name is "hotel"
    private static final String username = "yasmin";
    private static final String password = "Rasul@0786";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Use updated driver class
        } catch (Exception e) {
            System.out.println("Driver error: " + e.getMessage());
            return;
        }

        try (
            Connection conn = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in)
        ) {
            while (true) {
                System.out.println("\nHOTEL MANAGEMENT SYSTEM");
                System.out.println("--------------------------------------------------");
                System.out.println("1: Reserve Hotel Room");
                System.out.println("2: Update Guest Info");
                System.out.println("3: Get Room No by Guest Name");
                System.out.println("4: Delete Guest Reservation");
                System.out.println("5: View All Reservations");
                System.out.println("0: Exit");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        reserveRoom(conn, sc);
                        break;
                    case 2:
                        updateInfo(conn, sc);
                        break;
                    case 3:
                        getRoom(conn, sc);
                        break;
                    case 4:
                        vacatingRoom(conn, sc);
                        break;
                    case 5:
                        viewAll(conn);
                        break;
                    case 0:
                        System.out.println("EXITING.......!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    // ✅ Reserve a room
    static void reserveRoom(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter guest name: ");
            String name = sc.nextLine();

            System.out.print("Enter room number: ");
            int room = Integer.parseInt(sc.nextLine());

            System.out.print("Enter contact number (10 digits): ");
            String contact = sc.nextLine();

            String query = "INSERT INTO reservation (name, room_no, contact) VALUES ('"
                    + name + "', " + room + ", '" + contact + "')";

            Statement stmt = conn.createStatement();
            int result = stmt.executeUpdate(query);

            if (result > 0) {
                System.out.println("✅ Room reserved successfully.");
            } else {
                System.out.println("❌ Reservation failed.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // ✅ Update guest info
    private static void updateInfo(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter room number to update: ");
            int roomNo = Integer.parseInt(sc.nextLine());

            System.out.print("Enter new guest name: ");
            String newName = sc.nextLine();

            System.out.print("Enter new contact number: ");
            String newContact = sc.nextLine();

            String query = "UPDATE reservation SET name = '" + newName +
                           "', contact = '" + newContact + "' WHERE room_no = " + roomNo;

            Statement stmt = conn.createStatement();
            int updated = stmt.executeUpdate(query);

            if (updated > 0) {
                System.out.println("✅ Guest info updated successfully.");
            } else {
                System.out.println("❌ No reservation found with that room number.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // ✅ Get room number by guest name
    private static void getRoom(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter guest name to search: ");
            String guestName = sc.nextLine();

            String query = "SELECT room_no FROM reservation WHERE name = '" + guestName + "'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            boolean found = false;
            while (rs.next()) {
                int room = rs.getInt("room_no");
                System.out.println("✅ Room Number: " + room);
                found = true;
            }

            if (!found) {
                System.out.println("❌ No guest found with name: " + guestName);
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // ✅ Delete guest reservation by room number
    private static void vacatingRoom(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter room number to delete: ");
            int id = Integer.parseInt(sc.nextLine());

            String query = "DELETE FROM reservation WHERE room_no = " + id;

            Statement stmt = conn.createStatement();
            int deleted = stmt.executeUpdate(query);

            if (deleted > 0) {
                System.out.println("✅ Reservation deleted for reservation_id " + id);
            } else {
                System.out.println("❌ No reservation found with reservation_number: " + id);
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // ✅ View all reservations
    private static void viewAll(Connection conn) {
        try {
            String query = "SELECT * FROM reservation;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\nALL RESERVATIONS:");
            System.out.println("------------------------------------------------------------");
            System.out.printf("%-5s %-20s %-10s %-15s %-20s\n", "ID", "Name", "Room", "Contact", "Joining Date");
            System.out.println("------------------------------------------------------------");

            boolean any = false;
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int room = rs.getInt("room_no");
                String contact = rs.getString("contact");
                String date = rs.getString("joining_date");

                System.out.printf("%-5d %-20s %-10d %-15s %-20s\n", id, name, room, contact, date);
                any = true;
            }

            if (!any) {
                System.out.println("No reservations found.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
