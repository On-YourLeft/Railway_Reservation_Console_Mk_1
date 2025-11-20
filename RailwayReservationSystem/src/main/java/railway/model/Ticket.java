package railway.model;

import java.util.HashMap;

/**
 * Abstract Ticket class, inherited by specific ticket types (AC1/AC2/AC3/Sleeper/General).
 * Encapsulates common fields for any train ticket.
 */
public abstract class Ticket {
    protected String pnrNumber;
    protected String userId;
    protected String trainNumber;
    protected String passengerName;
    protected int age;
    protected String ticketClass;      // "AC1", "AC2", "AC3", "Sleeper", "General"
    protected String bookingDate;
    protected String status;           // "CONFIRMED", "RAC", "WAITLIST", "CANCELLED"
    protected double fare;
    protected int seatNum;             // Assigned seat number (or -1 if not allotted)
    protected int wlNumber;            // Position in waitlist (if WAITLIST)
    protected int racNumber;           // Position in RAC (if RAC)
    protected String journeyDate;      // For history and PNR reference (optional, set at booking)

    public Ticket() {
        this.seatNum = -1;        // -1 if not allotted
        this.wlNumber = 0;
        this.racNumber = 0;
        this.status = "WAITLIST";
        this.journeyDate = "";
    }

    public Ticket(String pnrNumber, String userId, String trainNumber, String passengerName,
                  int age, String ticketClass, String bookingDate,
                  String status, double fare, int seatNum, int wlNumber, int racNumber, String journeyDate) {
        this.pnrNumber = pnrNumber;
        this.userId = userId;
        this.trainNumber = trainNumber;
        this.passengerName = passengerName;
        this.age = age;
        this.ticketClass = ticketClass;
        this.bookingDate = bookingDate;
        this.status = status;
        this.fare = fare;
        this.seatNum = seatNum;
        this.wlNumber = wlNumber;
        this.racNumber = racNumber;
        this.journeyDate = journeyDate;
    }

    // Polymorphic fare calculation: pass the correct baseFare for the specific class
    public abstract double calculateFare(double baseFare);

    // ===== Getters and Setters =====
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

    public int getSeatNum() { return seatNum; }
    public void setSeatNum(int seatNum) { this.seatNum = seatNum; }

    public int getWlNumber() { return wlNumber; }
    public void setWlNumber(int wlNumber) { this.wlNumber = wlNumber; }

    public int getRacNumber() { return racNumber; }
    public void setRacNumber(int racNumber) { this.racNumber = racNumber; }

    public String getJourneyDate() { return journeyDate; }
    public void setJourneyDate(String journeyDate) { this.journeyDate = journeyDate; }

    // ===== Utility to Display Status =====
    public String getDisplayStatus() {
        if ("CONFIRMED".equalsIgnoreCase(status)) {
            return "CONFIRMED (Seat " + seatNum + ")";
        } else if ("RAC".equalsIgnoreCase(status)) {
            return "RAC/" + racNumber;
        } else if ("WAITLIST".equalsIgnoreCase(status)) {
            return "WL/" + wlNumber;
        } else if ("CANCELLED".equalsIgnoreCase(status)) {
            return "Cancelled";
        }
        return status;
    }
}
