package railway.storage;

import railway.entities.*;
import railway.auth.*;
import railway.model.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;

/**
 * FileManager handles all reading/writing to Excel (.xlsx) files.
 * Uses Apache POI for XLSX file operations.
 */
public class FileManager {
    // ========== TRAINS ==========
    public void saveTrains(ArrayList<Train> trains) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Trains");

        String[] headers = {"Train Number", "Train Name", "Source", "Destination",
                "Total Seats", "Available Seats", "Base Fare"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Train t : trains) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(t.getTrainNumber());
            row.createCell(1).setCellValue(t.getTrainName());
            row.createCell(2).setCellValue(t.getSource());
            row.createCell(3).setCellValue(t.getDestination());
            row.createCell(4).setCellValue(t.getTotalSeats());
            row.createCell(5).setCellValue(t.getAvailableSeats());
            row.createCell(6).setCellValue(t.getBaseFare());
        }
        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

        FileOutputStream fos = new FileOutputStream("data/trains.xlsx");
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    public ArrayList<Train> loadTrains() throws IOException {
        ArrayList<Train> trains = new ArrayList<>();
        File file = new File("data/trains.xlsx");
        if (!file.exists()) return trains;

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Train t = new Train(
                        row.getCell(0).getStringCellValue(),
                        row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(),
                        row.getCell(3).getStringCellValue(),
                        (int) row.getCell(4).getNumericCellValue(),
                        (int) row.getCell(5).getNumericCellValue(),
                        row.getCell(6).getNumericCellValue()
                );
                trains.add(t);
            }
        }
        workbook.close();
        fis.close();
        return trains;
    }

    // ========== CUSTOMERS ==========
    public void saveUsers(ArrayList<Customer> users) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Users");

        String[] headers = {"User ID", "Username", "Password", "Contact Number", "Registration Date"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Customer c : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(c.getUserId());
            row.createCell(1).setCellValue(c.getUsername());
            row.createCell(2).setCellValue(c.getPassword());
            row.createCell(3).setCellValue(c.getContactNumber());
            row.createCell(4).setCellValue(c.getRegistrationDate());
        }
        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

        FileOutputStream fos = new FileOutputStream("data/users.xlsx");
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    public ArrayList<Customer> loadUsers() throws IOException {
        ArrayList<Customer> users = new ArrayList<>();
        File file = new File("data/users.xlsx");
        if (!file.exists()) return users;

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Customer c = new Customer(
                        row.getCell(0).getStringCellValue(),
                        row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(),
                        row.getCell(3).getStringCellValue(),
                        row.getCell(4).getStringCellValue()
                );
                users.add(c);
            }
        }
        workbook.close();
        fis.close();
        return users;
    }

    // ========== ADMINS ==========
    public void saveAdmins(ArrayList<Admin> admins) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Admins");

        String[] headers = {"Admin ID", "Username", "Password", "Privilege Level", "Email"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Admin a : admins) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(a.getUserId());
            row.createCell(1).setCellValue(a.getUsername());
            row.createCell(2).setCellValue(a.getPassword());
            row.createCell(3).setCellValue(a.getPrivilegeLevel());
            row.createCell(4).setCellValue(a.getEmail());
        }
        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

        FileOutputStream fos = new FileOutputStream("data/admin.xlsx");
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    public ArrayList<Admin> loadAdmins() throws IOException {
        ArrayList<Admin> admins = new ArrayList<>();
        File file = new File("data/admin.xlsx");
        if (!file.exists()) return admins;

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Admin a = new Admin(
                        row.getCell(0).getStringCellValue(),
                        row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(),
                        row.getCell(3).getStringCellValue(),
                        row.getCell(4).getStringCellValue()
                );
                admins.add(a);
            }
        }
        workbook.close();
        fis.close();
        return admins;
    }

    // ========== RESERVATIONS/TICKETS ==========
    public void saveReservations(ArrayList<Ticket> tickets) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Reservations");

        String[] headers = {"PNR Number", "User ID", "Train Number", "Passenger Name",
                "Age", "Ticket Class", "Booking Date", "Status", "Fare"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Ticket t : tickets) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(t.getPnrNumber());
            row.createCell(1).setCellValue(t.getUserId());
            row.createCell(2).setCellValue(t.getTrainNumber());
            row.createCell(3).setCellValue(t.getPassengerName());
            row.createCell(4).setCellValue(t.getAge());
            row.createCell(5).setCellValue(t.getTicketClass());
            row.createCell(6).setCellValue(t.getBookingDate());
            row.createCell(7).setCellValue(t.getStatus());
            row.createCell(8).setCellValue(t.getFare());
        }
        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

        FileOutputStream fos = new FileOutputStream("data/reservations.xlsx");
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    public ArrayList<Ticket> loadReservations() throws IOException {
        ArrayList<Ticket> tickets = new ArrayList<>();
        File file = new File("data/reservations.xlsx");
        if (!file.exists()) return tickets;

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                String ticketClass = row.getCell(5).getStringCellValue();
                Ticket ticket;
                if ("AC".equalsIgnoreCase(ticketClass)) {
                    ticket = new ACClassTicket();
                } else {
                    ticket = new SleeperClassTicket();
                }
                // Set common fields
                ticket.setPnrNumber(row.getCell(0).getStringCellValue());
                ticket.setUserId(row.getCell(1).getStringCellValue());
                ticket.setTrainNumber(row.getCell(2).getStringCellValue());
                ticket.setPassengerName(row.getCell(3).getStringCellValue());
                ticket.setAge((int) row.getCell(4).getNumericCellValue());
                ticket.setTicketClass(ticketClass);
                ticket.setBookingDate(row.getCell(6).getStringCellValue());
                ticket.setStatus(row.getCell(7).getStringCellValue());
                ticket.setFare(row.getCell(8).getNumericCellValue());
                tickets.add(ticket);
            }
        }
        workbook.close();
        fis.close();
        return tickets;
    }

    // ========== PNR RECORDS ==========
    public void savePNRRecords(ArrayList<PNRRecord> records) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("PNR Records");

        String[] headers = {"PNR Number", "Train Number", "User ID", "Passenger Name", "Coach", "Seat Number",
                "Journey Date", "Booking Status", "Current Status", "Chart Prepared"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (PNRRecord pnr : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(pnr.getPnrNumber());
            row.createCell(1).setCellValue(pnr.getTrainNumber());
            row.createCell(2).setCellValue(pnr.getUserId());
            row.createCell(3).setCellValue(pnr.getPassengerName());
            row.createCell(4).setCellValue(pnr.getCoach());
            row.createCell(5).setCellValue(pnr.getSeatNumber());
            row.createCell(6).setCellValue(pnr.getJourneyDate());
            row.createCell(7).setCellValue(pnr.getBookingStatus());
            row.createCell(8).setCellValue(pnr.getCurrentStatus());
            row.createCell(9).setCellValue(pnr.isChartPrepared() ? "Yes" : "No");
        }
        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

        FileOutputStream fos = new FileOutputStream("data/pnr_records.xlsx");
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    public ArrayList<PNRRecord> loadPNRRecords() throws IOException {
        ArrayList<PNRRecord> records = new ArrayList<>();
        File file = new File("data/pnr_records.xlsx");
        if (!file.exists()) return records;

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                PNRRecord pnr = new PNRRecord(
                        row.getCell(0).getStringCellValue(),
                        row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(),
                        row.getCell(3).getStringCellValue(),
                        row.getCell(4).getStringCellValue(),
                        row.getCell(5).getStringCellValue(),
                        row.getCell(6).getStringCellValue(),
                        row.getCell(7).getStringCellValue(),
                        row.getCell(8).getStringCellValue(),
                        "Yes".equalsIgnoreCase(row.getCell(9).getStringCellValue())
                );
                records.add(pnr);
            }
        }
        workbook.close();
        fis.close();
        return records;
    }
}
