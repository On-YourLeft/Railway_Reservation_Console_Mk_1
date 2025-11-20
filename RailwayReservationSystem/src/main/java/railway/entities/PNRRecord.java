package railway.entities;

/**
 * PNRRecord stores all necessary info about a reservation for PNR status checks,
 * including current status (Confirmed/RAC/WL), coach/class, position in queue, and arrival history reference.
 */
public class PNRRecord {
    private String pnrNumber;
    private String trainNumber;
    private String userId;
    private String passengerName;
    private String coach;         // e.g., "A1", "S2", "GEN" or just the class type like "AC1"
    private String seatNumber;    // e.g., "17", "56", or "-" for RAC/WL
    private String journeyDate;
    private String ticketClass;   // "AC1", "AC2", "AC3", "Sleeper", "General"
    private String bookingStatus; // For admin/historical status: "BOOKED", "CANCELLED"
    private String currentStatus; // "CONFIRMED", "RAC", "WAITLIST", "CANCELLED"
    private int racNumber;        // If assigned to RAC queue (else 0)
    private int wlNumber;         // If assigned to waiting list (else 0)
    private boolean chartPrepared; // Has final allocation status been set?

    // For future expansion: train arrival history references, performance, etc.

    public PNRRecord(String pnrNumber, String trainNumber, String userId,
                     String passengerName, String coach, String seatNumber,
                     String journeyDate, String ticketClass, String bookingStatus,
                     String currentStatus, int racNumber, int wlNumber, boolean chartPrepared) {
        this.pnrNumber = pnrNumber;
        this.trainNumber = trainNumber;
        this.userId = userId;
        this.passengerName = passengerName;
        this.coach = coach;
        this.seatNumber = seatNumber;
        this.journeyDate = journeyDate;
        this.ticketClass = ticketClass;
        this.bookingStatus = bookingStatus;
        this.currentStatus = currentStatus;
        this.racNumber = racNumber;
        this.wlNumber = wlNumber;
        this.chartPrepared = chartPrepared;
    }

    // ======= Getters and Setters =======
    public String getPnrNumber() { return pnrNumber; }
    public String getTrainNumber() { return trainNumber; }
    public String getUserId() { return userId; }
    public String getPassengerName() { return passengerName; }
    public String getCoach() { return coach; }
    public void setCoach(String coach) { this.coach = coach; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public String getJourneyDate() { return journeyDate; }
    public String getTicketClass() { return ticketClass; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
    public int getRacNumber() { return racNumber; }
    public void setRacNumber(int racNumber) { this.racNumber = racNumber; }
    public int getWlNumber() { return wlNumber; }
    public void setWlNumber(int wlNumber) { this.wlNumber = wlNumber; }
    public boolean isChartPrepared() { return chartPrepared; }
    public void setChartPrepared(boolean chartPrepared) { this.chartPrepared = chartPrepared; }

    // Utility: Printable status
    public String getDisplayStatus() {
        if ("CONFIRMED".equalsIgnoreCase(currentStatus))
            return "CONFIRMED, Seat " + seatNumber + " (" + ticketClass + ")";
        else if ("RAC".equalsIgnoreCase(currentStatus))
            return "RAC/" + racNumber + " (" + ticketClass + ")";
        else if ("WAITLIST".equalsIgnoreCase(currentStatus))
            return "WL/" + wlNumber + " (" + ticketClass + ")";
        else if ("CANCELLED".equalsIgnoreCase(currentStatus))
            return "Cancelled";
        else return currentStatus;
    }
}
