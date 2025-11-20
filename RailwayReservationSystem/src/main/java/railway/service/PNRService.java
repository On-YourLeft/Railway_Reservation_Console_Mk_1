package railway.service;

import railway.entities.PNRRecord;
import railway.storage.FileManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles all PNR-related operations: status check, updates, and generation.
 * Fully supports multi-class, RAC/WL, and chart preparation status.
 */
public class PNRService {
    private FileManager fileManager;

    public PNRService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    // Check PNR status by PNR number (returns the full record, UI displays with .getDisplayStatus())
    public PNRRecord checkPNRStatus(String pnrNumber) throws IOException {
        ArrayList<PNRRecord> records = fileManager.loadPNRRecords();
        for (PNRRecord pnr : records) {
            if (pnr.getPnrNumber().equals(pnrNumber)) {
                return pnr;
            }
        }
        return null; // Not found
    }

    // Update PNR status and chart status (for admin use, e.g., during chart preparation or operator panel)
    public boolean updatePNRStatus(String pnrNumber, String newStatus, boolean chartPrepared,
                                   String coach, String seatNumber, int racNumber, int wlNumber) throws IOException {
        ArrayList<PNRRecord> records = fileManager.loadPNRRecords();
        boolean found = false;
        for (PNRRecord pnr : records) {
            if (pnr.getPnrNumber().equals(pnrNumber)) {
                // Use updated setters for expanded fields
                pnr.setCurrentStatus(newStatus);
                pnr.setChartPrepared(chartPrepared);
                if (coach != null) pnr.setCoach(coach);
                if (seatNumber != null) pnr.setSeatNumber(seatNumber);
                pnr.setRacNumber(racNumber);
                pnr.setWlNumber(wlNumber);
                found = true;
                break;
            }
        }
        if (found) {
            fileManager.savePNRRecords(records);
        }
        return found;
    }

    // Generate and save new PNR record (called after booking)
    public void generatePNR(PNRRecord record) throws IOException {
        ArrayList<PNRRecord> records = fileManager.loadPNRRecords();
        records.add(record);
        fileManager.savePNRRecords(records);
    }

    // Optional: List all PNRs for a user (for dashboard/history)
    public ArrayList<PNRRecord> getPNRsForUser(String userId) throws IOException {
        ArrayList<PNRRecord> records = fileManager.loadPNRRecords();
        ArrayList<PNRRecord> userPNRs = new ArrayList<>();
        for (PNRRecord pnr : records) {
            if (pnr.getUserId().equals(userId)) {
                userPNRs.add(pnr);
            }
        }
        return userPNRs;
    }
}
