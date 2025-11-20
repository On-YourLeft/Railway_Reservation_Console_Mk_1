package railway.model;

/**
 * SleeperClassTicket: ticket subclass for Sleeper and General class bookings.
 * Calculates fare with class-based multipliers or reductions.
 */
public class SleeperClassTicket extends Ticket {

    // Default constructor
    public SleeperClassTicket() {}

    // Custom constructor
    public SleeperClassTicket(String pnrNumber, String userId, String trainNumber, String passengerName,
                              int age, String ticketClass, String bookingDate, String status, double baseFare,
                              int seatNum, int wlNumber, int racNumber, String journeyDate) {
        super(pnrNumber, userId, trainNumber, passengerName, age, ticketClass, bookingDate, status, 0.0,
                seatNum, wlNumber, racNumber, journeyDate);
        this.fare = calculateFare(baseFare);
    }

    /**
     * Override fare calculation for Sleeper and General classes.
     * Sleeper = 1.2x, General = 1.0x multiplier. Adjust as per real/desired tariffs.
     */
    @Override
    public double calculateFare(double baseFare) {
        switch (ticketClass.toUpperCase()) {
            case "SLEEPER": return baseFare * 1.2;
            case "GENERAL": return baseFare * 1.0;
            default:        return baseFare * 1.2; // fallback for unknown type
        }
    }
}
