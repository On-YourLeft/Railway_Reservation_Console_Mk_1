package railway.auth;

import railway.entities.User;
import railway.storage.FileManager;
import railway.exception.InvalidInputException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles login, registration, and user authentication.
 */
public class AuthenticationService {
    private FileManager fileManager;

    public AuthenticationService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Authenticates user with username and password.
     * Checks both customers and admins.
     */
    public User login(String username, String password) throws InvalidInputException, IOException {
        ArrayList<Customer> customers = fileManager.loadUsers();
        ArrayList<Admin> admins = fileManager.loadAdmins();

        for (Customer c : customers) {
            if (c.getUsername().equals(username) && c.getPassword().equals(password)) {
                return c;
            }
        }
        for (Admin a : admins) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password)) {
                return a;
            }
        }

        throw new InvalidInputException("Invalid username or password.");
    }

    /**
     * Registers a new customer and saves to file.
     */
    public void registerCustomer(Customer customer) throws InvalidInputException, IOException {
        ArrayList<Customer> users = fileManager.loadUsers();
        for (Customer c : users) {
            if (c.getUsername().equals(customer.getUsername())) {
                throw new InvalidInputException("Username already exists.");
            }
        }
        users.add(customer);
        try {
            fileManager.saveUsers(users);
        } catch (Exception e) {
            throw new InvalidInputException("Error saving customer: " + e.getMessage());
        }
    }

    /**
     * Registers a new admin and saves to file.
     * (Optional, usually you add admins directly in the excel file for security)
     */
    public void registerAdmin(Admin admin) throws InvalidInputException, IOException {
        ArrayList<Admin> admins = fileManager.loadAdmins();
        for (Admin a : admins) {
            if (a.getUsername().equals(admin.getUsername())) {
                throw new InvalidInputException("Admin username already exists.");
            }
        }
        admins.add(admin);
        try {
            fileManager.saveAdmins(admins);
        } catch (Exception e) {
            throw new InvalidInputException("Error saving admin: " + e.getMessage());
        }
    }
}
