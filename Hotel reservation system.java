import java.io.*;
import java.util.*;

// Room class
class Room {
    int id;
    String type;
    double price;
    boolean isAvailable;

    Room(int id, String type, double price, boolean isAvailable) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
    }
}

// Booking class
class Booking {
    int bookingId;
    String customerName;
    int roomId;
    int nights;
    double totalAmount;
    boolean paid;

    Booking(int bookingId, String customerName, int roomId, int nights, double totalAmount, boolean paid) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomId = roomId;
        this.nights = nights;
        this.totalAmount = totalAmount;
        this.paid = paid;
    }
}

// Main system
public class HotelManagementSystem {

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    static Scanner sc = new Scanner(System.in);

    static final String ROOM_FILE = "rooms.txt";
    static final String BOOKING_FILE = "bookings.txt";

    public static void main(String[] args) {
        loadRooms();
        loadBookings();
        if (rooms.isEmpty()) initializeRooms();

        while (true) {
            System.out.println("\n===== HOTEL MANAGEMENT SYSTEM =====");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Bookings");
            System.out.println("5. Pay for Booking");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewRooms();
                case 2 -> bookRoom();
                case 3 -> cancelBooking();
                case 4 -> viewBookings();
                case 5 -> payBooking();
                case 6 -> {
                    saveRooms();
                    saveBookings();
                    System.out.println("Exiting... Data saved.");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // Initialize default rooms
    static void initializeRooms() {
        rooms.add(new Room(101, "Standard", 1000, true));
        rooms.add(new Room(102, "Standard", 1000, true));
        rooms.add(new Room(201, "Deluxe", 2000, true));
        rooms.add(new Room(202, "Deluxe", 2000, true));
        rooms.add(new Room(301, "Suite", 3500, true));
    }

    // View rooms
    static void viewRooms() {
        System.out.println("\n--- Available Rooms ---");
        for (Room r : rooms) {
            if (r.isAvailable) {
                System.out.println("Room " + r.id + " | " + r.type + " | ₹" + r.price);
            }
        }
    }

    // Book room
    static void bookRoom() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        viewRooms();
        System.out.print("Enter room ID to book: ");
        int id = sc.nextInt();

        System.out.print("Enter number of nights: ");
        int nights = sc.nextInt();

        for (Room r : rooms) {
            if (r.id == id && r.isAvailable) {
                r.isAvailable = false;

                double total = r.price * nights;
                int bookingId = bookings.size() + 1;

                bookings.add(new Booking(bookingId, name, id, nights, total, false));

                System.out.println("Booking successful! Booking ID: " + bookingId);
                System.out.println("Total Amount: ₹" + total);
                return;
            }
        }
        System.out.println("Room not available!");
    }

    // Cancel booking
    static void cancelBooking() {
        System.out.print("Enter booking ID to cancel: ");
        int id = sc.nextInt();

        for (Booking b : bookings) {
            if (b.bookingId == id) {
                for (Room r : rooms) {
                    if (r.id == b.roomId) {
                        r.isAvailable = true;
                    }
                }
                bookings.remove(b);
                System.out.println("Booking cancelled successfully.");
                return;
            }
        }
        System.out.println("Booking not found.");
    }

    // View bookings
    static void viewBookings() {
        System.out.println("\n--- Bookings ---");
        for (Booking b : bookings) {
            System.out.println("ID: " + b.bookingId +
                    " | Name: " + b.customerName +
                    " | Room: " + b.roomId +
                    " | Nights: " + b.nights +
                    " | Amount: ₹" + b.totalAmount +
                    " | Paid: " + b.paid);
        }
    }

    // Payment simulation
    static void payBooking() {
        System.out.print("Enter booking ID to pay: ");
        int id = sc.nextInt();

        for (Booking b : bookings) {
            if (b.bookingId == id) {
                if (b.paid) {
                    System.out.println("Already paid.");
                    return;
                }
                System.out.println("Processing payment of ₹" + b.totalAmount + "...");
                System.out.println("Payment successful!");
                b.paid = true;
                return;
            }
        }
        System.out.println("Booking not found.");
    }

    // Save rooms to file
    static void saveRooms() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ROOM_FILE))) {
            for (Room r : rooms) {
                pw.println(r.id + "," + r.type + "," + r.price + "," + r.isAvailable);
            }
        } catch (Exception e) {
            System.out.println("Error saving rooms.");
        }
    }

    // Load rooms
    static void loadRooms() {
        try (BufferedReader br = new BufferedReader(new FileReader(ROOM_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                rooms.add(new Room(
                        Integer.parseInt(p[0]),
                        p[1],
                        Double.parseDouble(p[2]),
                        Boolean.parseBoolean(p[3])
                ));
            }
        } catch (Exception e) {
            // file may not exist first time
        }
    }

    // Save bookings
    static void saveBookings() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOKING_FILE))) {
            for (Booking b : bookings) {
                pw.println(b.bookingId + "," + b.customerName + "," +
                        b.roomId + "," + b.nights + "," +
                        b.totalAmount + "," + b.paid);
            }
        } catch (Exception e) {
            System.out.println("Error saving bookings.");
        }
    }

    // Load bookings
    static void loadBookings() {
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKING_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                bookings.add(new Booking(
                        Integer.parseInt(p[0]),
                        p[1],
                        Integer.parseInt(p[2]),
                        Integer.parseInt(p[3]),
                        Double.parseDouble(p[4]),
                        Boolean.parseBoolean(p[5])
                ));
            }
        } catch (Exception e) {
            // ignore first run
        }
    }
}