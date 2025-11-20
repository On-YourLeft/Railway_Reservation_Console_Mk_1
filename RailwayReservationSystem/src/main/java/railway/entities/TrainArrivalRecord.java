package railway.entities;

/**
 * TrainArrivalRecord: Stores a record of a train's scheduled vs. actual arrival/departure.
 */
public class TrainArrivalRecord {
    private String runDate;       // Date of this instance/journey (YYYY-MM-DD)
    private String scheduledArrival;    // e.g. "10:23"
    private String actualArrival;       // e.g. "10:33"
    private String scheduledDeparture;
    private String actualDeparture;
    private boolean onTime;

    public TrainArrivalRecord(String runDate, String scheduledArrival, String actualArrival,
                              String scheduledDeparture, String actualDeparture, boolean onTime) {
        this.runDate = runDate;
        this.scheduledArrival = scheduledArrival;
        this.actualArrival = actualArrival;
        this.scheduledDeparture = scheduledDeparture;
        this.actualDeparture = actualDeparture;
        this.onTime = onTime;
    }

    // Getters/setters
    public String getRunDate() { return runDate; }
    public String getScheduledArrival() { return scheduledArrival; }
    public String getActualArrival() { return actualArrival; }
    public String getScheduledDeparture() { return scheduledDeparture; }
    public String getActualDeparture() { return actualDeparture; }
    public boolean isOnTime() { return onTime; }

    public void setRunDate(String runDate) { this.runDate = runDate; }
    public void setScheduledArrival(String scheduledArrival) { this.scheduledArrival = scheduledArrival; }
    public void setActualArrival(String actualArrival) { this.actualArrival = actualArrival; }
    public void setScheduledDeparture(String scheduledDeparture) { this.scheduledDeparture = scheduledDeparture; }
    public void setActualDeparture(String actualDeparture) { this.actualDeparture = actualDeparture; }
    public void setOnTime(boolean onTime) { this.onTime = onTime; }
}
