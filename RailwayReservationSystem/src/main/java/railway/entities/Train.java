package railway.entities;

/**
 * Train entity to store details about trains and seat availability.
 */
public class Train {
    private String trainNumber;
    private String trainName;
    private String source;
    private String destination;
    private int totalSeats;
    private int availableSeats;
    private double baseFare;

    public Train(String trainNumber, String trainName, String source, String destination,
                 int totalSeats, int availableSeats, double baseFare) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.baseFare = baseFare;
    }

    // Getters and Setters
    public String getTrainNumber() {
        return trainNumber;
    }
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getTotalSeats() {
        return totalSeats;
    }
    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getBaseFare() {
        return baseFare;
    }
    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }
}
