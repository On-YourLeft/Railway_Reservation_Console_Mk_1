package railway.service;

import railway.entities.PNRRecord;
import railway.storage.FileManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles all PNR-related operations: status check, updates, and generation.
 */
public class PNRService {
    private FileManager fileManager;

    public PNRService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    // Check PNR status by PNR number
    public PNRRecord checkPNRStatus(String pnrNumber) throws IOException {
        ArrayList<PNRRecord> records = fileManager.loadPNRRecords();
        for (PNRRecord pnr : records) {
            if (pnr.getPnrNumber().equals(pnrNumber)) {
                return pnr;
            }
        }
        return null; // Not found
    }

    // Update PNR status (for admin use)
    public boolean updatePNRStatus(String pnrNumber, String newStatus, boolean chartPrepared) throws IOException {
        ArrayList<PNRRecord> records = fileManager.loadPNRRecords();
        boolean found = false;
        for (PNRRecord pnr : records) {
            if (pnr.getPnrNumber().equals(pnrNumber)) {
                // Reflection or setters if you add them
                // (assuming you add setters for status and chartPrepared)
                // pnr.setCurrentStatus(newStatus);
                // pnr.setChartPrepared(chartPrepared);
                found = true;
                break;
            }
        }
        if (found) {
            fileManager.savePNRRecords(records);
        }
        return found;
    }

    // Generate and save new PNR record (used after booking)
    public void generatePNR(PNRRecord record) throws IOException {
        ArrayList<PNRRecord> records = fileManager.loadPNRRecords();
        records.add(record);
        fileManager.savePNRRecords(records);
    }
}
