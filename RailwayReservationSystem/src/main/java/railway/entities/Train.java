package railway.entities;

import railway.exception.InvalidInputException;
import railway.model.Ticket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import railway.entities.ClassSeatInfo;


/**
 * Train entity to store details about trains, multi-class seat availability, queues, and fares—Indian Railways style.
 */
public class Train {
    private String trainNumber;
    private String trainName;
    private String source;
    private String destination;

    // Each class type (e.g., "AC1", "AC2", "AC3", "Sleeper", "General")
    private HashMap<String, ClassSeatInfo> seatClasses;


    public Train(String trainNumber, String trainName, String source, String destination, HashMap<String, ClassSeatInfo> seatClasses) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.seatClasses = seatClasses;
    }

    // ============ Basic Info Getters/Setters ============
    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public HashMap<String, ClassSeatInfo> getSeatClasses() { return seatClasses; }
    public void setSeatClasses(HashMap<String, ClassSeatInfo> seatClasses) { this.seatClasses = seatClasses; }

    /** Get all valid class names for selection by user */
    public ArrayList<String> getAvailableClasses() {
        return new ArrayList<>(seatClasses.keySet());
    }

    // ============ Multi-Class Seat Map ============
    public void printSeatMap(String classType) {
        if (!seatClasses.containsKey(classType)) {
            System.out.println("Class " + classType + " not available for this train.");
            return;
        }
        ClassSeatInfo info = seatClasses.get(classType);
        System.out.println("**** Seat Map for Train " + trainNumber + " (" + classType + ") ****");
        boolean[] seats = info.seats;
        for (int i = 0; i < seats.length; i++) {
            System.out.print(seats[i] ? "[" + classType.charAt(0) + "X]" : "[" + classType.charAt(0) + "O]");
            // Newline after every 10 seats for readability
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println();
    }

    // ============ Example: Find base fare for a class ============
    public double getBaseFare(String classType) {
        if (seatClasses.containsKey(classType)) {
            return seatClasses.get(classType).baseFare;
        }
        return -1;
    }
}

/**
 * Encapsulates all data for a single seat class in a train (AC1, AC2, etc.)
 */
