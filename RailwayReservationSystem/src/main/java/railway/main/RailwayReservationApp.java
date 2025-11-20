package railway.main;

import railway.entities.*;
import railway.auth.*;
import railway.model.*;
import railway.service.*;
import railway.storage.*;
import railway.exception.InvalidInputException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RailwayReservationApp {
    private static FileManager fileManager = new FileManager();
    private static AuthenticationService authService = new AuthenticationService(fileManager);
    private static TrainManagementService trainManagement = new TrainManagementService(fileManager);
    private static ReservationSystem reservationSystem = new ReservationSystem(fileManager);
    private static PNRService pnrService = new PNRService(fileManager);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("==== Welcome to Railway Reservation System ====");

        while (true) {
            System.out.println();
            System.out.println("1. Customer Login");
            System.out.println("2. Admin Login");
            System.out.println("3. Register as Customer");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            try {
                switch (choice) {
                    case 1:
                        customerLogin(sc);
                        break;
                    case 2:
                        adminLogin(sc);
                        break;
                    case 3:
                        registerCustomer(sc);
                        break;
                    case 4:
                        System.out.println("Exiting... Thank you!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (InvalidInputException | IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ===== Customer Login & Dashboard =====
    private static void customerLogin(Scanner sc) throws InvalidInputException, IOException {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        User user = authService.login(username, password);

        if (user instanceof Customer) {
            customerMenu((Customer) user, sc);
        } else {
            System.out.println("Not a customer login.");
        }
    }

    private static void customerMenu(Customer customer, Scanner sc) throws IOException, InvalidInputException {
        while (true) {
            System.out.println();
            System.out.println("==== Customer Dashboard ====");
            System.out.println("1. Search Trains");
            System.out.println("2. Book Ticket");
            System.out.println("3. My Bookings");
            System.out.println("4. Check PNR Status");
            System.out.println("5. Cancel My Ticket");
            System.out.println("6. Update Profile");
            System.out.println("7. View Train Seat Map");
            System.out.println("8. Logout");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    searchTrains(sc);
                    break;
                case 2:
                    bookTicket(customer, sc);
                    break;
                case 3:
                    viewBookings(customer, sc);
                    break;
                case 4:
                    checkPNR(sc);
                    break;
                case 5:
                    cancelMyTicket(customer, sc);
                    break;
                case 6:
                    updateProfile(customer, sc);
                    break;
                case 7:
                    viewTrainSeatMap(sc);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== Admin Login & Dashboard =====

    private static void adminLogin(Scanner sc) throws InvalidInputException, IOException {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        User user = authService.login(username, password);

        if (user instanceof Admin) {
            adminMenu((Admin) user, sc);
        } else {
            System.out.println("Not an admin login.");
        }
    }

    private static void adminMenu(Admin admin, Scanner sc) throws IOException, InvalidInputException {
        while (true) {
            System.out.println();
            System.out.println("==== Admin Dashboard ====");
            System.out.println("1. Add New Train");
            System.out.println("2. Update Train Details");
            System.out.println("3. View All Bookings");
            System.out.println("4. Cancel Any Ticket");
            System.out.println("5. Generate Reports");
            System.out.println("6. Manage Fare Rules");
            System.out.println("7. View Registered Users");
            System.out.println("8. Logout");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    addNewTrain(sc);
                    break;
                case 2:
                    updateTrain(sc);
                    break;
                case 3:
                    viewAllBookings(sc);
                    break;
                case 4:
                    cancelAnyTicket(sc);
                    break;
                case 5:
                    generateReports(sc);
                    break;
                case 6:
                    manageFareRules(sc);
                    break;
                case 7:
                    viewRegisteredUsers(sc);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== Registration =====

    private static void registerCustomer(Scanner sc) throws InvalidInputException, IOException {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        System.out.print("Contact Number: ");
        String phone = sc.nextLine();
        String userId = "USER" + System.currentTimeMillis();
        String registrationDate = java.time.LocalDate.now().toString();

        Customer customer = new Customer(userId, username, password, phone, registrationDate);
        authService.registerCustomer(customer);
        System.out.println("Registered successfully! Please login now.");
    }

    // ===== Customer Methods =====

    private static void searchTrains(Scanner sc) throws IOException {
        System.out.print("Source: ");
        String src = sc.nextLine();
        System.out.print("Destination: ");
        String dest = sc.nextLine();

        ArrayList<Train> trains = trainManagement.searchTrains(src, dest);
        if (trains.isEmpty()) {
            System.out.println("No trains found.");
        } else {
            for (Train t : trains) {
                System.out.println("Train: " + t.getTrainName() + " [" + t.getTrainNumber() + "]");
                for (String classType : t.getAvailableClasses()) {
                    System.out.println("  - Class: " + classType +
                            " | Seats Left: " + t.getSeatClasses().get(classType).availableSeats +
                            " | Fare: " + t.getSeatClasses().get(classType).baseFare);
                }
                System.out.println("--------------------");
            }
        }
    }

    private static void bookTicket(Customer customer, Scanner sc) throws IOException, InvalidInputException {
        System.out.print("Train Number: ");
        String trainNum = sc.nextLine();
        ArrayList<Train> trains = fileManager.loadTrains();
        Train selTrain = null;
        for (Train t : trains) {
            if (t.getTrainNumber().equals(trainNum)) {
                selTrain = t;
                break;
            }
        }
        if (selTrain == null) {
            System.out.println("Train not found.");
            return;
        }
        System.out.println("Available Classes: " + String.join(", ", selTrain.getAvailableClasses()));
        System.out.print("Enter Class for Booking: ");
        String classType = sc.nextLine().toUpperCase();

        if (!selTrain.getSeatClasses().containsKey(classType)) {
            System.out.println("Class not found for train.");
            return;
        }
        selTrain.printSeatMap(classType);

        System.out.print("Passenger Name: ");
        String name = sc.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("ID Proof: ");
        String idProof = sc.nextLine();

        String bookingDate = java.time.LocalDate.now().toString();
        Passenger passenger = new Passenger(name, age, idProof);

        // Modified: now pass classType for true multi-class
        Ticket ticket = reservationSystem.bookTicket(selTrain, passenger, customer.getUserId(), bookingDate);

        System.out.println("Booking Successful! PNR: " + ticket.getPnrNumber());
        System.out.println("Your status: " + ticket.getDisplayStatus());
        selTrain.printSeatMap(classType);
    }

    private static void viewBookings(Customer customer, Scanner sc) throws IOException {
        ArrayList<Ticket> myTickets = reservationSystem.viewBookings(customer.getUserId());
        if (myTickets.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Ticket t : myTickets) {
                System.out.println("PNR: " + t.getPnrNumber() +
                        " | Train: " + t.getTrainNumber() +
                        " | Date: " + t.getBookingDate() +
                        " | Class: " + t.getTicketClass() +
                        " | Status: " + t.getDisplayStatus());
            }
        }
    }

    private static void checkPNR(Scanner sc) throws IOException {
        System.out.print("Enter PNR Number: ");
        String pnr = sc.nextLine();
        PNRRecord record = pnrService.checkPNRStatus(pnr);
        if (record == null) {
            System.out.println("PNR Not Found.");
        } else {
            System.out.println("Train: " + record.getTrainNumber() + " | Passenger: " + record.getPassengerName());
            System.out.println("Status: " + record.getDisplayStatus() +
                    " | Chart Prepared: " + (record.isChartPrepared() ? "Yes" : "No"));
        }
    }

    private static void cancelMyTicket(Customer customer, Scanner sc) throws IOException, InvalidInputException {
        System.out.print("Enter PNR Number to Cancel: ");
        String pnr = sc.nextLine();
        ArrayList<Ticket> myTickets = reservationSystem.viewBookings(customer.getUserId());
        boolean isOwner = myTickets.stream().anyMatch(t -> t.getPnrNumber().equals(pnr));
        if (isOwner) {
            reservationSystem.cancelTicket(pnr);
            System.out.println("Ticket cancelled successfully.");
            // Optionally show latest seat map per class
        } else {
            System.out.println("You are not the owner of this ticket.");
        }
    }

    private static void updateProfile(Customer customer, Scanner sc) throws InvalidInputException, IOException {
        System.out.print("New Contact Number: ");
        String newPhone = sc.nextLine();
        customer.setContactNumber(newPhone);
        ArrayList<Customer> users = fileManager.loadUsers();
        for (Customer c : users) {
            if (c.getUserId().equals(customer.getUserId())) {
                c.setContactNumber(newPhone);
                break;
            }
        }
        try {
            fileManager.saveUsers(users);
            System.out.println("Contact updated.");
        } catch (IOException e) {
            throw new InvalidInputException("Error updating profile.");
        }
    }

    // ====== NEW METHOD FOR CUSTOMER =======
    private static void viewTrainSeatMap(Scanner sc) throws IOException {
        System.out.print("Enter Train Number: ");
        String trainNum = sc.nextLine();
        ArrayList<Train> trains = fileManager.loadTrains();
        Train train = null;
        for (Train t : trains) {
            if (t.getTrainNumber().equals(trainNum)) {
                train = t;
                break;
            }
        }
        if (train != null) {
            System.out.println("Available Classes: " + String.join(", ", train.getAvailableClasses()));
            System.out.print("Enter Class to View Map: ");
            String classType = sc.nextLine().toUpperCase();
            if (!train.getSeatClasses().containsKey(classType)) {
                System.out.println("Class not found for train.");
                return;
            }
            train.printSeatMap(classType);
        } else {
            System.out.println("Train not found.");
        }
    }

    // ====== Admin Methods ======

    private static void addNewTrain(Scanner sc) throws IOException {
        System.out.print("Train Number: ");
        String tNum = sc.nextLine();
        System.out.print("Train Name: ");
        String tName = sc.nextLine();
        System.out.print("Source: ");
        String src = sc.nextLine();
        System.out.print("Destination: ");
        String dest = sc.nextLine();

        // Multiclass input: prompt for number of classes
        System.out.print("How many classes does this train have? ");
        int classCount = Integer.parseInt(sc.nextLine());
        HashMap<String, ClassSeatInfo> seatClasses = new HashMap<>();
        for (int i = 0; i < classCount; i++) {
            System.out.print("Class " + (i + 1) + " (e.g. AC1, AC2, AC3, SLEEPER, GENERAL): ");
            String classType = sc.nextLine().toUpperCase();
            System.out.print("Seats in " + classType + ": ");
            int totalSeats = Integer.parseInt(sc.nextLine());
            System.out.print("Base fare in " + classType + ": ");
            double baseFare = Double.parseDouble(sc.nextLine());
            System.out.print("Max RAC bookings in " + classType + ": ");
            int maxRac = Integer.parseInt(sc.nextLine());
            System.out.print("Max Waitlist in " + classType + ": ");
            int maxWL = Integer.parseInt(sc.nextLine());
            seatClasses.put(classType, new ClassSeatInfo(totalSeats, baseFare, maxRac, maxWL));
        }
        Train train = new Train(tNum, tName, src, dest, seatClasses);
        trainManagement.addTrain(train);
        System.out.println("Train added.");
    }

    private static void updateTrain(Scanner sc) throws IOException {
        System.out.print("Train Number to Update: ");
        String tNum = sc.nextLine();
        System.out.print("New Train Name: ");
        String tName = sc.nextLine();
        System.out.print("New Source: ");
        String src = sc.nextLine();
        System.out.print("New Destination: ");
        String dest = sc.nextLine();

        // Repeat class input logic from addNewTrain
        System.out.print("How many classes does this train have? ");
        int classCount = Integer.parseInt(sc.nextLine());
        HashMap<String, ClassSeatInfo> seatClasses = new HashMap<>();
        for (int i = 0; i < classCount; i++) {
            System.out.print("Class " + (i + 1) + " (e.g. AC1, AC2, AC3, SLEEPER, GENERAL): ");
            String classType = sc.nextLine().toUpperCase();
            System.out.print("Seats in " + classType + ": ");
            int totalSeats = Integer.parseInt(sc.nextLine());
            System.out.print("Base fare in " + classType + ": ");
            double baseFare = Double.parseDouble(sc.nextLine());
            System.out.print("Max RAC bookings in " + classType + ": ");
            int maxRac = Integer.parseInt(sc.nextLine());
            System.out.print("Max Waitlist in " + classType + ": ");
            int maxWL = Integer.parseInt(sc.nextLine());
            seatClasses.put(classType, new ClassSeatInfo(totalSeats, baseFare, maxRac, maxWL));
        }
        Train updatedTrain = new Train(tNum, tName, src, dest, seatClasses);
        trainManagement.updateTrain(tNum, updatedTrain);
        System.out.println("Train updated.");
    }

    private static void viewAllBookings(Scanner sc) throws IOException {
        ArrayList<Ticket> tickets = fileManager.loadReservations();
        for (Ticket t : tickets) {
            System.out.println("PNR: " + t.getPnrNumber() +
                    " | User: " + t.getUserId() +
                    " | Train: " + t.getTrainNumber() +
                    " | Class: " + t.getTicketClass() +
                    " | Status: " + t.getDisplayStatus());
        }
    }

    private static void cancelAnyTicket(Scanner sc) throws IOException, InvalidInputException {
        System.out.print("Enter PNR to cancel: ");
        String pnr = sc.nextLine();
        reservationSystem.cancelTicket(pnr);
        System.out.println("Ticket cancelled (admin override).");
    }

    private static void generateReports(Scanner sc) throws IOException {
        ArrayList<Ticket> tickets = fileManager.loadReservations();
        int totalBookings = tickets.size();
        double totalRevenue = 0;
        for (Ticket t : tickets) {
            totalRevenue += t.getFare();
        }
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: " + totalRevenue);
    }

    private static void manageFareRules(Scanner sc) {
        System.out.println("Fare rules are managed in your AC/Sleeper Ticket subclasses. Edit them for new rules.");
    }

    private static void viewRegisteredUsers(Scanner sc) throws IOException {
        ArrayList<Customer> users = fileManager.loadUsers();
        for (Customer u : users) {
            System.out.println("User: " + u.getUsername() +
                    " | Phone: " + u.getContactNumber() +
                    " | Registered: " + u.getRegistrationDate());
        }
    }
}
