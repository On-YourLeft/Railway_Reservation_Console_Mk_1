package railway.service;

import railway.entities.*;
import railway.model.*;
import railway.storage.FileManager;
import railway.exception.InvalidInputException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles customer and admin reservations, booking, cancellation, and viewing bookings.
 */
public class ReservationSystem {
    private FileManager fileManager;

    public ReservationSystem(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    // Book ticket (creates AC/Sleeper ticket based on class)
    public Ticket bookTicket(Train train, Passenger passenger, String userId, String ticketClass, String bookingDate)
            throws IOException, InvalidInputException {

        // Seat availability check
        if (train.getAvailableSeats() <= 0) {
            throw new InvalidInputException("No seats available on this train.");
        }

        // Generate PNR
        String pnr = "PNR" + System.currentTimeMillis();

        // Calculate fare via polymorphism
        Ticket ticket;
        if ("AC".equalsIgnoreCase(ticketClass)) {
            ticket = new ACClassTicket(pnr, userId, train.getTrainNumber(), passenger.getName(),
                    passenger.getAge(), bookingDate, "CONFIRMED", train.getBaseFare());
        } else {
            ticket = new SleeperClassTicket(pnr, userId, train.getTrainNumber(), passenger.getName(),
                    passenger.getAge(), bookingDate, "CONFIRMED", train.getBaseFare());
        }

        // Save booking, update seats
        ArrayList<Ticket> tickets = fileManager.loadReservations();
        tickets.add(ticket);
        fileManager.saveReservations(tickets);

        // Update train seat count
        ArrayList<Train> trains = fileManager.loadTrains();
        for (Train t : trains) {
            if (t.getTrainNumber().equals(train.getTrainNumber())) {
                t.setAvailableSeats(t.getAvailableSeats() - 1);
                break;
            }
        }
        fileManager.saveTrains(trains);

        return ticket;
    }

    // Cancel ticket by PNR
    public boolean cancelTicket(String pnrNumber) throws IOException, InvalidInputException {
        ArrayList<Ticket> tickets = fileManager.loadReservations();
        boolean found = tickets.removeIf(t -> t.getPnrNumber().equals(pnrNumber));
        if (!found) throw new InvalidInputException("Ticket not found.");
        fileManager.saveReservations(tickets);
        return true;
    }

    // View all bookings for a user (customer dashboard)
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

    // Check seat availability for a train
    public int checkSeatAvailability(String trainNumber) throws IOException {
        ArrayList<Train> trains = fileManager.loadTrains();
        for (Train t : trains) {
            if (t.getTrainNumber().equals(trainNumber)) {
                return t.getAvailableSeats();
            }
        }
        return 0;
    }
}
