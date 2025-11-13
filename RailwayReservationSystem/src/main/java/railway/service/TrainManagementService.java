package railway.service;

import railway.entities.Train;
import railway.storage.FileManager;
import railway.exception.InvalidInputException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles admin operations: add/update/delete/search trains.
 */
public class TrainManagementService {
    private FileManager fileManager;

    public TrainManagementService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    // Add a train to the system
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

    // Update train details
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

    // Search trains by source/destination
    public ArrayList<Train> searchTrains(String source, String destination) throws IOException {
        ArrayList<Train> trains = fileManager.loadTrains();
        ArrayList<Train> result = new ArrayList<>();
        for (Train t : trains) {
            if (t.getSource().equalsIgnoreCase(source) &&
                    t.getDestination().equalsIgnoreCase(destination))
            {
                result.add(t);
            }
        }
        return result;
    }
}
