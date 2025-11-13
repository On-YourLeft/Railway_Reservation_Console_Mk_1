package railway.auth;

import railway.entities.User;

/**
 * Admin class represents a system administrator.
 * Extends the base User class.
 */
public class Admin extends User {
    private String privilegeLevel;   // e.g., "SUPERUSER", "STATION_LEVEL"
    private String email;

    public Admin(String userId, String username, String password, String privilegeLevel, String email) {
        super(userId, username, password);
        this.privilegeLevel = privilegeLevel;
        this.email = email;
    }

    // Getter and setter for privilegeLevel
    public String getPrivilegeLevel() {
        return privilegeLevel;
    }
    public void setPrivilegeLevel(String privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
    }

    // Getter and setter for email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUserType() {
        return "ADMIN";
    }
}
