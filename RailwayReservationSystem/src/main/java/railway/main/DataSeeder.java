package railway.main;

import railway.auth.Admin;
import railway.auth.Customer;
import railway.entities.ClassSeatInfo;
import railway.entities.Train;
import railway.storage.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * UPDATED DATA SEEDER:
 * Generates 200+ Trains, 25 Users, and 2 Admins.
 * Run this 'main' method once to populate your 'data' folder.
 */
public class DataSeeder {

    // Major Indian Cities for Route Generation
    private static final String[] CITIES = {
            "New Delhi", "Mumbai Central", "Howrah", "Chennai Central", "Bangalore",
            "Secunderabad", "Ahmedabad", "Pune", "Patna", "Jaipur", "Lucknow",
            "Trivandrum", "Bhopal", "Guwahati", "Bhubaneswar", "Ranchi", "Dehradun",
            "Varanasi", "Amritsar", "Jammu Tawi", "Goa (Madgaon)", "Nagpur", "Indore",
            "Surat", "Vadodara", "Kanpur", "Allahabad", "Gorakhpur", "Coimbatore"
    };

    public static void main(String[] args) {
        System.out.println("=== Railway Data Seeder (Extended) ===");

        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdir();
        }

        FileManager fileManager = new FileManager();

        try {
            // 1. Clear old data to prevent duplicates if run multiple times
            new File("data/trains.xlsx").delete();
            new File("data/users.xlsx").delete();
            new File("data/reservations.xlsx").delete();
            new File("data/pnr_records.xlsx").delete();

            // 2. Seed Data
            seedAdmins(fileManager);
            seedUsers(fileManager); // Now generates 25 users
            seedTrains(fileManager); // Now generates 200 trains

            System.out.println("\nSUCCESS! Database populated with:");
            System.out.println("- 2 Admins");
            System.out.println("- 25 Sample Customers");
            System.out.println("- 200 Major Trains (Rajdhani, Shatabdi, Express, etc.)");

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

    private static void seedUsers(FileManager fm) throws IOException {
        ArrayList<Customer> users = new ArrayList<>();
        String[] names = {"Ayush", "Rahul", "Sneha", "Priya", "Vikram", "Anjali", "Rohit", "Kavita", "Amit", "Divya"};

        // Generate 25 Users
        for (int i = 1; i <= 25; i++) {
            String name = names[i % names.length] + " " + (char)('A' + i);
            String uid = "USR" + String.format("%03d", i);
            String phone = "98" + String.format("%08d", i * 1234);
            users.add(new Customer(uid, "User" + i, "pass" + i, phone, "2025-01-" + (i % 28 + 1)));
        }

        fm.saveUsers(users);
        System.out.print(".");
    }

    private static void seedTrains(FileManager fm) throws IOException {
        ArrayList<Train> trains = new ArrayList<>();
        Random rand = new Random();
        int trainCount = 0;

        // --- A. Generate Special Trains (Rajdhani/Shatabdi) ---
        // Link major cities to Delhi (typical Rajdhani routes)
        for (int i = 1; i < CITIES.length && trainCount < 50; i++) {
            // Train from City -> Delhi
            trains.add(createTrain("12" + String.format("%03d", trainCount),
                    CITIES[i] + " Rajdhani", CITIES[i], "New Delhi", "RAJDHANI"));
            trainCount++;

            // Train from Delhi -> City
            trains.add(createTrain("22" + String.format("%03d", trainCount),
                    CITIES[i] + " Rajdhani", "New Delhi", CITIES[i], "RAJDHANI"));
            trainCount++;
        }

        // --- B. Generate Express/Superfast Trains ---
        // Randomly connect other cities
        while (trainCount < 200) {
            String source = CITIES[rand.nextInt(CITIES.length)];
            String dest = CITIES[rand.nextInt(CITIES.length)];

            if (source.equals(dest)) continue; // Skip invalid routes

            String type = "EXPRESS";
            int r = rand.nextInt(10);
            if (r < 2) type = "SHATABDI"; // 20% chance
            else if (r < 4) type = "GARIB_RATH"; // 20% chance
            else if (r < 9) type = "EXPRESS"; // 50% chance
            else type = "GENERAL"; // 10% chance

            String tNum = (1 + rand.nextInt(9)) + "" + String.format("%04d", rand.nextInt(9999));
            String tName = source + " - " + dest + " " + (type.equals("GENERAL") ? "Passenger" : type);

            // Fix naming for display
            tName = tName.replace("SHATABDI", "Shatabdi Exp")
                    .replace("GARIB_RATH", "Garib Rath")
                    .replace("EXPRESS", "Superfast Exp");

            trains.add(createTrain(tNum, tName, source, dest, type));
            trainCount++;
        }

        fm.saveTrains(trains);
        System.out.println(".");
    }

    // Helper to create a Train object with correct Seat Configuration
    private static Train createTrain(String num, String name, String src, String dest, String type) {
        HashMap<String, ClassSeatInfo> seatClasses = new HashMap<>();

        // Base fares are approximated based on dummy distance logic
        double baseFare = 500.0 + (name.length() * 20);

        switch (type) {
            case "RAJDHANI":
                // AC First, AC 2-Tier, AC 3-Tier
                seatClasses.put("AC1", new ClassSeatInfo(24, baseFare * 3.0, 5, 10));
                seatClasses.put("AC2", new ClassSeatInfo(50, baseFare * 2.0, 10, 25));
                seatClasses.put("AC3", new ClassSeatInfo(120, baseFare * 1.5, 25, 75));
                break;

            case "SHATABDI":
                // Executive Chair, AC Chair Car
                seatClasses.put("EXEC", new ClassSeatInfo(40, baseFare * 2.5, 5, 10));
                seatClasses.put("CC", new ClassSeatInfo(150, baseFare * 1.2, 20, 50));
                break;

            case "GARIB_RATH":
                // Only AC3 (Economy)
                seatClasses.put("AC3", new ClassSeatInfo(400, baseFare * 0.8, 50, 150));
                break;

            case "GENERAL":
                // Only General Unreserved (Simulated as booked seats for system)
                seatClasses.put("GENERAL", new ClassSeatInfo(200, baseFare * 0.3, 0, 0));
                break;

            case "EXPRESS":
            default:
                // Standard mix: AC2, AC3, Sleeper, General
                seatClasses.put("AC2", new ClassSeatInfo(40, baseFare * 2.0, 10, 20));
                seatClasses.put("AC3", new ClassSeatInfo(64, baseFare * 1.5, 20, 50));
                seatClasses.put("SLEEPER", new ClassSeatInfo(300, baseFare * 0.6, 50, 200));
                seatClasses.put("GENERAL", new ClassSeatInfo(100, baseFare * 0.3, 0, 0));
                break;
        }

        return new Train(num, name, src, dest, seatClasses);
    }
}