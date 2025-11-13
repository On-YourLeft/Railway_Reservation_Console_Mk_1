package railway.model;

/**
 * SleeperClassTicket: ticket subclass for Sleeper class bookings.
 * Calculates fare with the base multiplier (no change).
 */
public class SleeperClassTicket extends Ticket {
    // Default constructor
    public SleeperClassTicket() {}

    // Custom constructor
    public SleeperClassTicket(String pnrNumber, String userId, String trainNumber, String passengerName,
                              int age, String bookingDate, String status, double baseFare) {
        super(pnrNumber, userId, trainNumber, passengerName, age, "SLEEPER", bookingDate, status, 0.0);
        this.fare = calculateFare(baseFare);
    }

    // Override fare calculation (Polymorphism)
    @Override
    public double calculateFare(double baseFare) {
        return baseFare * 1.0;
    }
}
