package railway.service;

import railway.entities.Train;
import railway.entities.ClassSeatInfo;
import railway.storage.FileManager;
import railway.exception.InvalidInputException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles admin operations: add/update/delete/search trains, fully compatible with multi-class model.
 */
public class TrainManagementService {
    private FileManager fileManager;

    public TrainManagementService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    // Add a train with multiple seat classes
    public void addTrain(Train train) throws IOException {
        ArrayList<Train> trains = fileManager.loadTrains();
        for (Train t : trains) {
            if (t.getTrainNumber().equals(train.getTrainNumber())) {
                throw new IOException("Train with this number already exists.");
            }
        }
        trains.add(train);
        fileManager.saveTrains(trains);
    }

    // Update train details (including seat classes, quotas, fares, and queues)
    public void updateTrain(String trainNumber, Train updatedTrain) throws IOException {
        ArrayList<Train> trains = fileManager.loadTrains();
        boolean found = false;
        for (int i = 0; i < trains.size(); i++) {
            if (trains.get(i).getTrainNumber().equals(trainNumber)) {
                trains.set(i, updatedTrain);
                found = true;
                break;
            }
        }
        if (!found) throw new IOException("Train not found.");
        fileManager.saveTrains(trains);
    }

    // Delete train by number
    public void deleteTrain(String trainNumber) throws IOException {
        ArrayList<Train> trains = fileManager.loadTrains();
        boolean found = trains.removeIf(t -> t.getTrainNumber().equals(trainNumber));
        if (!found) throw new IOException("Train not found.");
        fileManager.saveTrains(trains);
    }

    // Search trains by source/destination (case-insensitive, partial match)
    public ArrayList<Train> searchTrains(String source, String destination) throws IOException {
        ArrayList<Train> result = new ArrayList<>();
        ArrayList<Train> allTrains = fileManager.loadTrains();

        String srcLower = source.trim().toLowerCase();
        String destLower = destination.trim().toLowerCase();

        for (Train t : allTrains) {
            String trainSource = t.getSource().toLowerCase();
            String trainDest = t.getDestination().toLowerCase();
            if (trainSource.contains(srcLower) && trainDest.contains(destLower)) {
                result.add(t);
            }
        }
        return result;
    }

    // Optional: search by class availability (show only trains with seats in given class)
    public ArrayList<Train> searchTrainsByClass(String source, String destination, String classType) throws IOException {
        ArrayList<Train> result = new ArrayList<>();
        ArrayList<Train> allTrains = fileManager.loadTrains();

        String srcLower = source.trim().toLowerCase();
        String destLower = destination.trim().toLowerCase();

        for (Train t : allTrains) {
            String trainSource = t.getSource().toLowerCase();
            String trainDest = t.getDestination().toLowerCase();
            if (trainSource.contains(srcLower) && trainDest.contains(destLower)) {
                HashMap<String, ClassSeatInfo> classes = t.getSeatClasses();
                if (classes.containsKey(classType) && classes.get(classType).availableSeats > 0) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    // Optional: get train by train number
    public Train searchTrainByNumber(String trainNumber) throws IOException {
        ArrayList<Train> allTrains = fileManager.loadTrains();
        for (Train t : allTrains) {
            if (t.getTrainNumber().equals(trainNumber)) return t;
        }
        return null;
    }
}
