package railway.entities;

/**
 * Base User class used for authentication.
 * Extended by Admin and Customer.
 */
public abstract class User {
    protected String userId;
    protected String username;
    protected String password;

    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    // User type (to be implemented in subclasses)
    public abstract String getUserType();
}
