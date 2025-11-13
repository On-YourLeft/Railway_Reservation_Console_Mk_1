package railway.model;

/**
 * Abstract Ticket class, inherited by specific ticket types (AC/Sleeper).
 * Encapsulates common fields for any train ticket.
 */
public abstract class Ticket {
    protected String pnrNumber;
    protected String userId;
    protected String trainNumber;
    protected String passengerName;
    protected int age;
    protected String ticketClass;
    protected String bookingDate;
    protected String status; // e.g., CONFIRMED, WAITLIST, RAC
    protected double fare;

    public Ticket() {
        // Default constructor
    }

    public Ticket(String pnrNumber, String userId, String trainNumber, String passengerName,
                  int age, String ticketClass, String bookingDate,
                  String status, double fare) {
        this.pnrNumber = pnrNumber;
        this.userId = userId;
        this.trainNumber = trainNumber;
        this.passengerName = passengerName;
        this.age = age;
        this.ticketClass = ticketClass;
        this.bookingDate = bookingDate;
        this.status = status;
        this.fare = fare;
    }

    // Abstract method, used for polymorphism
    public abstract double calculateFare(double baseFare);

    // Getters and setters
    public String getPnrNumber() { return pnrNumber; }
    public void setPnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getTicketClass() { return ticketClass; }
    public void setTicketClass(String ticketClass) { this.ticketClass = ticketClass; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }
}
