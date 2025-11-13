package railway.auth;

import railway.entities.User;

/**
 * Customer class represents a railway customer.
 * Extends the base User class.
 */
public class Customer extends User {
    private String contactNumber;            // Customer's phone number
    private String registrationDate;         // Date of registration

    public Customer(String userId, String username, String password, String contactNumber, String registrationDate) {
        super(userId, username, password);
        this.contactNumber = contactNumber;
        this.registrationDate = registrationDate;
    }

    // Getter and setter for contactNumber
    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    // Getter and setter for registrationDate
    public String getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String getUserType() {
        return "CUSTOMER";
    }
}
