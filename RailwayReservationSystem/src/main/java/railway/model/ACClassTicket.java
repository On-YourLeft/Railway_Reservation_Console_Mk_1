package railway.model;

/**
 * ACClassTicket: ticket subclass for AC classes (AC1, AC2, AC3).
 * Calculates fare with class-based multipliers as per IRCTC style.
 */
public class ACClassTicket extends Ticket {

    // Default constructor
    public ACClassTicket() {}

    // Custom constructor
    public ACClassTicket(String pnrNumber, String userId, String trainNumber, String passengerName,
                         int age, String ticketClass, String bookingDate, String status, double baseFare,
                         int seatNum, int wlNumber, int racNumber, String journeyDate) {
        super(pnrNumber, userId, trainNumber, passengerName, age, ticketClass, bookingDate, status, 0.0,
                seatNum, wlNumber, racNumber, journeyDate);
        this.fare = calculateFare(baseFare);
    }

    /**
     * Override fare calculation based on class type.
     * AC1 = 3.5x, AC2 = 2.7x, AC3 = 2.2x multiplier (examples—customize multipliers as needed)
     */
    @Override
    public double calculateFare(double baseFare) {
        // Use the dynamic multiplier from FareConfig
        double multiplier = FareConfig.getMultiplier(ticketClass);
        // Fallback logic if "AC" generic is used (optional)
        if (multiplier == 1.0 && ticketClass.toUpperCase().startsWith("AC")) {
            return baseFare * 2.5;
        }
        return baseFare * multiplier;
    }
}
