package railway.model;

/**
 * ACClassTicket: ticket subclass for AC class bookings.
 * Calculates fare with a higher multiplier.
 */
public class ACClassTicket extends Ticket {
    // Default constructor
    public ACClassTicket() {}

    // Custom constructor
    public ACClassTicket(String pnrNumber, String userId, String trainNumber, String passengerName,
                         int age, String bookingDate, String status, double baseFare) {
        super(pnrNumber, userId, trainNumber, passengerName, age, "AC", bookingDate, status, 0.0);
        this.fare = calculateFare(baseFare);
    }

    // Override fare calculation (Polymorphism)
    @Override
    public double calculateFare(double baseFare) {
        return baseFare * 2.5; // Example multiplier for AC class
    }
}
