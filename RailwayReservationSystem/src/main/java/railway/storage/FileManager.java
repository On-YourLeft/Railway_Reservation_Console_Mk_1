package railway.storage;

import railway.entities.*;
import railway.auth.*;
import railway.model.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManager {

    // ========== TRAINS with MULTI-CLASS ==========
    public void saveTrains(ArrayList<Train> trains) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Trains");

        String[] trainHeaders = {"Train Number", "Train Name", "Source", "Destination", "ClassData"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < trainHeaders.length; i++) {
            headerRow.createCell(i).setCellValue(trainHeaders[i]);
        }

        int rowNum = 1;
        for (Train t : trains) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(t.getTrainNumber());
            row.createCell(1).setCellValue(t.getTrainName());
            row.createCell(2).setCellValue(t.getSource());
            row.createCell(3).setCellValue(t.getDestination());
            // Serialize each class as: classType:totalSeats|availSeats|baseFare|seatMap|maxRac|maxWL|confirmed:rac:wl;
            HashMap<String, ClassSeatInfo> classes = t.getSeatClasses();
            StringBuilder classData = new StringBuilder();
            for (String classType : classes.keySet()) {
                ClassSeatInfo info = classes.get(classType);
                classData.append(classType).append(":")
                        .append(info.totalSeats).append("|").append(info.availableSeats).append("|")
                        .append(info.baseFare).append("|");
                for (boolean b : info.seats) classData.append(b ? "X" : "O");
                classData.append("|").append(info.maxRacSeats).append("|").append(info.maxWaitlist).append("|");
                classData.append(info.confirmedTickets.size()).append(":")
                        .append(info.racQueue.size()).append(":").append(info.waitlistQueue.size());
                classData.append(";");
            }
            row.createCell(4).setCellValue(classData.toString());
        }
        for (int i = 0; i < trainHeaders.length; i++) sheet.autoSizeColumn(i);

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
                String trainNumber = row.getCell(0).getStringCellValue();
                String trainName = row.getCell(1).getStringCellValue();
                String source = row.getCell(2).getStringCellValue();
                String destination = row.getCell(3).getStringCellValue();
                String classData = row.getCell(4).getStringCellValue();
                HashMap<String, ClassSeatInfo> seatClasses = new HashMap<>();
                for (String block : classData.split(";")) {
                    if (block.trim().isEmpty()) continue;
                    String[] mainParts = block.split(":");
                    String classType = mainParts[0];
                    String[] details = mainParts[1].split("\\|");
                    int totalSeats = Integer.parseInt(details[0]);
                    int availableSeats = Integer.parseInt(details[1]);
                    double baseFare = Double.parseDouble(details[2]);
                    String seatMapStr = details[3];
                    int maxRac = Integer.parseInt(details[4]);
                    int maxWL = Integer.parseInt(details[5]);
                    String[] queueSizes = details[6].split(":");
                    ClassSeatInfo info = new ClassSeatInfo(totalSeats, baseFare, maxRac, maxWL);
                    info.availableSeats = availableSeats;
                    for (int j = 0; j < seatMapStr.length(); j++)
                        info.seats[j] = (seatMapStr.charAt(j) == 'X');
                    // Note: For demo, just keep queue size meta (tickets reloaded from reservations)
                    seatClasses.put(classType, info);
                }
                trains.add(new Train(trainNumber, trainName, source, destination, seatClasses));
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

    // ========== RESERVATIONS/TICKETS (with all new Ticket fields) ==========
    public void saveReservations(ArrayList<Ticket> tickets) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Reservations");
        String[] headers = {
                "PNR Number", "User ID", "Train Number", "Passenger Name", "Age", "Ticket Class",
                "Booking Date", "Status", "Fare", "Seat Num", "WL Num", "RAC Num", "Journey Date"
        };
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
            row.createCell(9).setCellValue(t.getSeatNum());
            row.createCell(10).setCellValue(t.getWlNumber());
            row.createCell(11).setCellValue(t.getRacNumber());
            row.createCell(12).setCellValue(t.getJourneyDate());
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
                if (ticketClass.toUpperCase().startsWith("AC")) {
                    ticket = new ACClassTicket();
                } else {
                    ticket = new SleeperClassTicket();
                }
                ticket.setPnrNumber(row.getCell(0).getStringCellValue());
                ticket.setUserId(row.getCell(1).getStringCellValue());
                ticket.setTrainNumber(row.getCell(2).getStringCellValue());
                ticket.setPassengerName(row.getCell(3).getStringCellValue());
                ticket.setAge((int) row.getCell(4).getNumericCellValue());
                ticket.setTicketClass(ticketClass);
                ticket.setBookingDate(row.getCell(6).getStringCellValue());
                ticket.setStatus(row.getCell(7).getStringCellValue());
                ticket.setFare(row.getCell(8).getNumericCellValue());
                ticket.setSeatNum((int) row.getCell(9).getNumericCellValue());
                ticket.setWlNumber((int) row.getCell(10).getNumericCellValue());
                ticket.setRacNumber((int) row.getCell(11).getNumericCellValue());
                ticket.setJourneyDate(row.getCell(12).getStringCellValue());
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
        String[] headers = {
                "PNR Number", "Train Number", "User ID", "Passenger Name", "Coach", "Seat Number",
                "Journey Date", "Ticket Class", "Booking Status", "Current Status",
                "RAC Number", "WL Number", "Chart Prepared"
        };
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
            row.createCell(7).setCellValue(pnr.getTicketClass());
            row.createCell(8).setCellValue(pnr.getBookingStatus());
            row.createCell(9).setCellValue(pnr.getCurrentStatus());
            row.createCell(10).setCellValue(pnr.getRacNumber());
            row.createCell(11).setCellValue(pnr.getWlNumber());
            row.createCell(12).setCellValue(pnr.isChartPrepared() ? "Yes" : "No");
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
                        row.getCell(9).getStringCellValue(),
                        (int) row.getCell(10).getNumericCellValue(),
                        (int) row.getCell(11).getNumericCellValue(),
                        "Yes".equalsIgnoreCase(row.getCell(12).getStringCellValue())
                );
                records.add(pnr);
            }
        }
        workbook.close();
        fis.close();
        return records;
    }
}
