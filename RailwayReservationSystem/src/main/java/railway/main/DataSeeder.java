package railway.main;

import railway.auth.Admin;
import railway.auth.Customer;
import railway.entities.ClassSeatInfo;
import railway.entities.PNRRecord;
import railway.entities.Train;
import railway.model.ACClassTicket;
import railway.model.SleeperClassTicket;
import railway.model.Ticket;
import railway.storage.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * EXTENDED DATA SEEDER:
 * Generates Users, Admins, Trains, AND Active Reservations.
 */
public class DataSeeder {

    // Major Indian Cities
    private static final String[] CITIES = {
            "New Delhi", "Mumbai Central", "Howrah", "Chennai Central", "Bangalore",
            "Secunderabad", "Ahmedabad", "Pune", "Patna", "Jaipur", "Lucknow",
            "Trivandrum", "Bhopal", "Guwahati", "Bhubaneswar", "Ranchi", "Dehradun",
            "Varanasi", "Amritsar", "Jammu Tawi", "Goa (Madgaon)", "Nagpur", "Indore",
            "Surat", "Vadodara", "Kanpur", "Allahabad", "Gorakhpur", "Coimbatore"
    };

    public static void main(String[] args) {
        System.out.println("=== Railway Data Seeder (Final) ===");

        File directory = new File("data");
        if (!directory.exists()) directory.mkdir();

        FileManager fileManager = new FileManager();

        try {
            // 1. Clear old data
            new File("data/trains.xlsx").delete();
            new File("data/users.xlsx").delete();
            new File("data/reservations.xlsx").delete();
            new File("data/pnr_records.xlsx").delete();

            // 2. Seed Base Data
            seedAdmins(fileManager);
            ArrayList<Customer> users = seedUsers(fileManager);
            ArrayList<Train> trains = seedTrains(fileManager);

            // 3. Seed Reservations (The new part)
            seedReservations(fileManager, trains, users);

            System.out.println("\nSUCCESS! Database populated with:");
            System.out.println("- 2 Admins & " + users.size() + " Customers");
            System.out.println("- " + trains.size() + " Trains");
            System.out.println("- 50+ Active Reservations & PNRs");

        } catch (IOException e) {
            System.out.println("Error seeding data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void seedAdmins(FileManager fm) throws IOException {
        ArrayList<Admin> admins = new ArrayList<>();
        admins.add(new Admin("ADM001", "ALPHA", "ALPHA123", "SUPERUSER", "admin@railway.com"));
        admins.add(new Admin("ADM002", "MANAGER", "PASS123", "STATION_MASTER", "manager@railway.com"));
        fm.saveAdmins(admins);
        System.out.print(".");
    }

    private static ArrayList<Customer> seedUsers(FileManager fm) throws IOException {
        ArrayList<Customer> users = new ArrayList<>();
        String[] names = {"Ayush", "Rahul", "Sneha", "Priya", "Vikram", "Anjali", "Rohit", "Kavita", "Amit", "Divya"};
        for (int i = 1; i <= 25; i++) {
            String name = names[i % names.length] + " " + (char)('A' + i);
            String uid = "USR" + String.format("%03d", i);
            String phone = "98" + String.format("%08d", i * 1234);
            users.add(new Customer(uid, "User" + i, "pass" + i, phone, "2025-01-" + (i % 28 + 1)));
        }
        fm.saveUsers(users);
        System.out.print(".");
        return users;
    }

    private static ArrayList<Train> seedTrains(FileManager fm) throws IOException {
        ArrayList<Train> trains = new ArrayList<>();
        Random rand = new Random();
        int trainCount = 0;

        // A. Special Trains
        for (int i = 1; i < CITIES.length && trainCount < 50; i++) {
            trains.add(createTrain("12" + String.format("%03d", trainCount), CITIES[i] + " Rajdhani", CITIES[i], "New Delhi", "RAJDHANI"));
            trainCount++;
            trains.add(createTrain("22" + String.format("%03d", trainCount), CITIES[i] + " Rajdhani", "New Delhi", CITIES[i], "RAJDHANI"));
            trainCount++;
        }

        // B. Express Trains
        while (trainCount < 200) {
            String source = CITIES[rand.nextInt(CITIES.length)];
            String dest = CITIES[rand.nextInt(CITIES.length)];
            if (source.equals(dest)) continue;

            String type = "EXPRESS";
            int r = rand.nextInt(10);
            if (r < 2) type = "SHATABDI";
            else if (r < 4) type = "GARIB_RATH";
            else if (r < 9) type = "EXPRESS";
            else type = "GENERAL";

            String tNum = (1 + rand.nextInt(9)) + "" + String.format("%04d", rand.nextInt(9999));
            String tName = source + " - " + dest + " " + (type.equals("GENERAL") ? "Passenger" : type);
            tName = tName.replace("SHATABDI", "Shatabdi Exp").replace("GARIB_RATH", "Garib Rath").replace("EXPRESS", "Superfast Exp");

            trains.add(createTrain(tNum, tName, source, dest, type));
            trainCount++;
        }
        // Note: We DON'T save trains yet, we wait until seats are booked in seedReservations
        System.out.println(".");
        return trains;
    }

    // ====== NEW: GENERATE RESERVATIONS ======
    private static void seedReservations(FileManager fm, ArrayList<Train> trains, ArrayList<Customer> users) throws IOException {
        ArrayList<Ticket> tickets = new ArrayList<>();
        ArrayList<PNRRecord> pnrRecords = new ArrayList<>();
        Random rand = new Random();

        System.out.print("Booking Tickets");

        // Generate 60 random bookings
        for (int i = 0; i < 60; i++) {
            Train train = trains.get(rand.nextInt(trains.size()));
            Customer user = users.get(rand.nextInt(users.size()));

            // Pick a random class available on this train
            ArrayList<String> classes = train.getAvailableClasses();
            if (classes.isEmpty()) continue;
            String ticketClass = classes.get(rand.nextInt(classes.size()));

            ClassSeatInfo seatInfo = train.getSeatClasses().get(ticketClass);

            // Only book if seats are available (simplifying to avoid complex RAC logic here)
            if (seatInfo.availableSeats > 0) {
                // Find first empty seat
                int seatNum = -1;
                for(int s=0; s < seatInfo.seats.length; s++) {
                    if(!seatInfo.seats[s]) {
                        seatInfo.seats[s] = true; // Mark occupied
                        seatNum = s + 1;
                        seatInfo.availableSeats--;
                        break;
                    }
                }

                if (seatNum != -1) {
                    // Create Ticket
                    String pnr = "PNR" + System.currentTimeMillis() + i;
                    Ticket t;
                    if(ticketClass.startsWith("AC")) t = new ACClassTicket();
                    else t = new SleeperClassTicket();

                    t.setPnrNumber(pnr);
                    t.setUserId(user.getUserId());
                    t.setTrainNumber(train.getTrainNumber());
                    t.setPassengerName("Passenger " + i);
                    t.setAge(20 + rand.nextInt(40));
                    t.setTicketClass(ticketClass);
                    t.setBookingDate("2025-11-21");
                    t.setStatus("CONFIRMED");
                    t.setFare(seatInfo.baseFare * (ticketClass.equals("AC1")?3.5 : 1.5)); // Approx calc
                    t.setSeatNum(seatNum);
                    t.setJourneyDate("2025-11-25");

                    tickets.add(t);

                    // Create PNR Record
                    PNRRecord p = new PNRRecord(pnr, train.getTrainNumber(), user.getUserId(),
                            t.getPassengerName(), ticketClass, String.valueOf(seatNum),
                            t.getJourneyDate(), ticketClass, "BOOKED", "CONFIRMED", 0, 0, false);
                    pnrRecords.add(p);
                }
            }
            if (i % 10 == 0) System.out.print(".");
        }

        // Save everything
        fm.saveReservations(tickets);
        fm.savePNRRecords(pnrRecords);
        fm.saveTrains(trains); // Save trains with updated seat maps
    }

    private static Train createTrain(String num, String name, String src, String dest, String type) {
        HashMap<String, ClassSeatInfo> seatClasses = new HashMap<>();
        double baseFare = 500.0 + (name.length() * 20);

        switch (type) {
            case "RAJDHANI":
                seatClasses.put("AC1", new ClassSeatInfo(24, baseFare * 3.0, 5, 10));
                seatClasses.put("AC2", new ClassSeatInfo(50, baseFare * 2.0, 10, 25));
                seatClasses.put("AC3", new ClassSeatInfo(120, baseFare * 1.5, 25, 75));
                break;
            case "SHATABDI":
                seatClasses.put("EXEC", new ClassSeatInfo(40, baseFare * 2.5, 5, 10));
                seatClasses.put("CC", new ClassSeatInfo(150, baseFare * 1.2, 20, 50));
                break;
            case "GARIB_RATH":
                seatClasses.put("AC3", new ClassSeatInfo(400, baseFare * 0.8, 50, 150));
                break;
            case "GENERAL":
                seatClasses.put("GENERAL", new ClassSeatInfo(200, baseFare * 0.3, 0, 0));
                break;
            default:
                seatClasses.put("AC2", new ClassSeatInfo(40, baseFare * 2.0, 10, 20));
                seatClasses.put("AC3", new ClassSeatInfo(64, baseFare * 1.5, 20, 50));
                seatClasses.put("SLEEPER", new ClassSeatInfo(300, baseFare * 0.6, 50, 200));
                seatClasses.put("GENERAL", new ClassSeatInfo(100, baseFare * 0.3, 0, 0));
                break;
        }
        return new Train(num, name, src, dest, seatClasses);
    }
}