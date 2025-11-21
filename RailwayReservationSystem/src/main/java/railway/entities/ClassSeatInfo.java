package railway.entities;

import railway.exception.InvalidInputException;
import railway.model.Ticket;

import java.util.ArrayList;
import java.util.LinkedList;

public class ClassSeatInfo {
    public int totalSeats;
    public int availableSeats;
    public double baseFare;
    public boolean[] seats;

    // Reservation Queues for this class
    public ArrayList<Ticket> confirmedTickets;
    public LinkedList<Ticket> racQueue;
    public LinkedList<Ticket> waitlistQueue;
    public int maxRacSeats;
    public int maxWaitlist;

    public ClassSeatInfo(int totalSeats, double baseFare, int maxRacSeats, int maxWaitlist) {
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.baseFare = baseFare;
        this.seats = new boolean[totalSeats];

        this.confirmedTickets = new ArrayList<>();
        this.racQueue = new LinkedList<>();
        this.waitlistQueue = new LinkedList<>();
        this.maxRacSeats = maxRacSeats;
        this.maxWaitlist = maxWaitlist;
    }

    // ======= Seat Booking/Cancel =======
    public boolean seatsAvailable() { return availableSeats > 0; }
    public boolean racAvailable() { return racQueue.size() < maxRacSeats; }
    public boolean waitlistAvailable() { return waitlistQueue.size() < maxWaitlist; }

    public String assignTicket(Ticket t) {
        if (seatsAvailable()) {
            confirmedTickets.add(t);
            return "CONFIRMED";
        } else if (racAvailable()) {
            racQueue.add(t);
            t.setRacNumber(racQueue.size());
            return "RAC";
        } else if (waitlistAvailable()) {
            waitlistQueue.add(t);
            t.setWlNumber(waitlistQueue.size());
            return "WAITLIST";
        } else {
            return "NOT_AVAILABLE";
        }
    }

    public void bookSeat(int seatNum) throws InvalidInputException {
        if (seatNum < 1 || seatNum > seats.length)
            throw new InvalidInputException("Invalid seat number");
        if (seats[seatNum - 1])
            throw new InvalidInputException("That seat is already booked!");
        seats[seatNum - 1] = true;
        availableSeats--;
    }

    public void cancelSeat(int seatNum) throws InvalidInputException {
        if (seatNum < 1 || seatNum > seats.length)
            throw new InvalidInputException("Invalid seat number");
        if (!seats[seatNum - 1])
            throw new InvalidInputException("That seat is not currently booked!");
        seats[seatNum - 1] = false;
        availableSeats++;
    }

    /** Promote ticket from RAC/waitlist to confirmed, called on seat cancellation or chart preparation. */
    public void promoteTickets() {
        if (!racQueue.isEmpty()) {
            Ticket racTicket = racQueue.poll();
            confirmedTickets.add(racTicket);
            racTicket.setStatus("CONFIRMED");
            if (!waitlistQueue.isEmpty() && racAvailable()) {
                Ticket wlTicket = waitlistQueue.poll();
                racQueue.add(wlTicket);
                wlTicket.setStatus("RAC");
                wlTicket.setRacNumber(racQueue.size());
            }
        } else if (!waitlistQueue.isEmpty() && seatsAvailable()) {
            Ticket wlTicket = waitlistQueue.poll();
            confirmedTickets.add(wlTicket);
            wlTicket.setStatus("CONFIRMED");
        }
    }

    /** Chart Preparation: promote all RAC to confirmed, all WL to RAC if space */
    public void chartPreparation() {
        while (seatsAvailable() && !racQueue.isEmpty()) {
            Ticket racTicket = racQueue.poll();
            confirmedTickets.add(racTicket);
            racTicket.setStatus("CONFIRMED");
        }
        while (racAvailable() && !waitlistQueue.isEmpty()) {
            Ticket wlTicket = waitlistQueue.poll();
            racQueue.add(wlTicket);
            wlTicket.setStatus("RAC");
            wlTicket.setRacNumber(racQueue.size());
        }
        // All others on waitlist stay as-is
    }
}
