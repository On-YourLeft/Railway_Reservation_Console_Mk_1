package railway.entities;

/**
 * PNRRecord stores all necessary info about a reservation for PNR status checks.
 */
public class PNRRecord {
    private String pnrNumber;
    private String trainNumber;
    private String userId;
    private String passengerName;
    private String coach;
    private String seatNumber;
    private String journeyDate;
    private String bookingStatus;
    private String currentStatus;
    private boolean chartPrepared;

    public PNRRecord(String pnrNumber, String trainNumber, String userId,
                     String passengerName, String coach, String seatNumber,
                     String journeyDate, String bookingStatus,
                     String currentStatus, boolean chartPrepared) {
        this.pnrNumber = pnrNumber;
        this.trainNumber = trainNumber;
        this.userId = userId;
        this.passengerName = passengerName;
        this.coach = coach;
        this.seatNumber = seatNumber;
        this.journeyDate = journeyDate;
        this.bookingStatus = bookingStatus;
        this.currentStatus = currentStatus;
        this.chartPrepared = chartPrepared;
    }

    // Getters and Setters
    public String getPnrNumber() { return pnrNumber; }
    public String getTrainNumber() { return trainNumber; }
    public String getUserId() { return userId; }
    public String getPassengerName() { return passengerName; }
    public String getCoach() { return coach; }
    public String getSeatNumber() { return seatNumber; }
    public String getJourneyDate() { return journeyDate; }
    public String getBookingStatus() { return bookingStatus; }
    public String getCurrentStatus() { return currentStatus; }
    public boolean isChartPrepared() { return chartPrepared; }

    // You can add setters if you want to edit the records later
}
