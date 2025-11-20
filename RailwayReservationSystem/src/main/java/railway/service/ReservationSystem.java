package railway.service;

import railway.entities.*;
import railway.model.*;
import railway.storage.FileManager;
import railway.exception.InvalidInputException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Handles customer and admin reservations, booking, cancellation, and viewing bookings.
 */
public class ReservationSystem {
    private FileManager fileManager;

    public ReservationSystem(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    // Book ticket with multi-class, queue-aware logic
    public Ticket bookTicket(Train train, Passenger passenger, String userId, String journeyDate)
            throws IOException, InvalidInputException {

        HashMap<String, ClassSeatInfo> classes = train.getSeatClasses();
        Scanner sc = new Scanner(System.in);

        // List all available classes to the user
        ArrayList<String> availableClasses = train.getAvailableClasses();
        System.out.println("Available Classes: " + String.join(", ", availableClasses));
        System.out.print("Select class for booking: ");
        String ticketClass = sc.nextLine().trim();

        if (!classes.containsKey(ticketClass)) {
            throw new InvalidInputException("Class not found for this train.");
        }

        ClassSeatInfo selectedClass = classes.get(ticketClass);

        // Print seat map before selection
        train.printSeatMap(ticketClass);

        // Booking logic: confirmed → RAC → waitlist
        String bookingStatus;
        int seatNum = -1, wlNumber = 0, racNumber = 0;
        double fare = selectedClass.baseFare;
        String status;

        if (selectedClass.seatsAvailable()) {
            // Confirmed booking
            System.out.print("Enter seat number to book (1-" + selectedClass.totalSeats + "): ");
            seatNum = sc.nextInt();
            sc.nextLine(); // consume
            selectedClass.bookSeat(seatNum); // throws if error
            selectedClass.availableSeats--;
            status = "CONFIRMED";
        } else if (selectedClass.racAvailable()) {
            racNumber = selectedClass.racQueue.size() + 1;
            status = "RAC";
        } else if (selectedClass.waitlistAvailable()) {
            wlNumber = selectedClass.waitlistQueue.size() + 1;
            status = "WAITLIST";
        } else {
            throw new InvalidInputException("No booking options available in this class.");
        }

        // Generate PNR
        String pnr = "PNR" + System.currentTimeMillis();

        // Create the ticket (use your relevant concrete ticket class OR Ticket directly)
        Ticket ticket = new ACClassTicket(
                pnr,
                userId,
                train.getTrainNumber(),
                passenger.getName(),
                passenger.getAge(),
                ticketClass,
                journeyDate,
                status,
                fare,
                seatNum,
                wlNumber,
                racNumber,
                journeyDate
        );

        // Assign to appropriate queue
        String queueStatus = selectedClass.assignTicket(ticket); // internally handles status for RAC/WL too

        // Save to persistent storage (add to main ticket list and update train list)
        ArrayList<Ticket> tickets = fileManager.loadReservations();
        tickets.add(ticket);
        fileManager.saveReservations(tickets);

        ArrayList<Train> trains = fileManager.loadTrains();
        for (Train t : trains) {
            if (t.getTrainNumber().equals(train.getTrainNumber())) {
                t.setSeatClasses(classes); // store updated queue/seat info
                break;
            }
        }
        fileManager.saveTrains(trains);

        // Show seat map after booking
        train.printSeatMap(ticketClass);

        System.out.println("Booking successful! Your status: " + ticket.getDisplayStatus());

        return ticket;
    }

    // Cancel ticket by PNR (promote RAC/WL if needed)
    public boolean cancelTicket(String pnrNumber) throws IOException, InvalidInputException {
        ArrayList<Ticket> tickets = fileManager.loadReservations();
        Ticket ticketToCancel = null;
        for (Ticket t : tickets) {
            if (t.getPnrNumber().equals(pnrNumber)) {
                ticketToCancel = t;
                break;
            }
        }
        if (ticketToCancel == null) throw new InvalidInputException("Ticket not found.");

        // Find train and class to cancel from
        ArrayList<Train> trains = fileManager.loadTrains();
        for (Train train : trains) {
            if (train.getTrainNumber().equals(ticketToCancel.getTrainNumber())) {
                HashMap<String, ClassSeatInfo> classes = train.getSeatClasses();
                if (!classes.containsKey(ticketToCancel.getTicketClass())) {
                    throw new InvalidInputException("Class not found for this train.");
                }
                ClassSeatInfo selectedClass = classes.get(ticketToCancel.getTicketClass());

                // Remove from relevant queue and free seat if applicable
                if ("CONFIRMED".equalsIgnoreCase(ticketToCancel.getStatus()) && ticketToCancel.getSeatNum() > 0) {
                    selectedClass.confirmedTickets.remove(ticketToCancel);
                    selectedClass.cancelSeat(ticketToCancel.getSeatNum());
                    selectedClass.availableSeats++;
                    selectedClass.promoteTickets();
                } else if ("RAC".equalsIgnoreCase(ticketToCancel.getStatus())) {
                    selectedClass.racQueue.remove(ticketToCancel);
                    selectedClass.promoteTickets();
                } else if ("WAITLIST".equalsIgnoreCase(ticketToCancel.getStatus())) {
                    selectedClass.waitlistQueue.remove(ticketToCancel);
                }
                fileManager.saveTrains(trains);
                train.printSeatMap(ticketToCancel.getTicketClass());
                break;
            }
        }
        boolean found = tickets.remove(ticketToCancel);
        fileManager.saveReservations(tickets);
        System.out.println("Booking cancelled. Status released: " + ticketToCancel.getDisplayStatus());
        return found;
    }

    // View all bookings for a user
    public ArrayList<Ticket> viewBookings(String userId) throws IOException {
        ArrayList<Ticket> tickets = fileManager.loadReservations();
        ArrayList<Ticket> myTickets = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getUserId().equals(userId)) {
                myTickets.add(t);
            }
        }
        return myTickets;
    }

    // Check seat availability for a train class
    public int checkSeatAvailability(String trainNumber, String classType) throws IOException {
        ArrayList<Train> trains = fileManager.loadTrains();
        for (Train t : trains) {
            if (t.getTrainNumber().equals(trainNumber)) {
                HashMap<String, ClassSeatInfo> classes = t.getSeatClasses();
                if (classes.containsKey(classType)) {
                    return classes.get(classType).availableSeats;
                }
            }
        }
        return 0;
    }
}
