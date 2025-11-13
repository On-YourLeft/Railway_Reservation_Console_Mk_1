package railway.exception;

/**
 * Custom exception for handling invalid input or application errors
 * in the Railway Reservation system.
 *
 * Use this exception wherever you want to reject bad user input,
 * signal booking issues, or handle other domain-specific errors
 * (e.g., user not found, seat not available).
 */
public class InvalidInputException extends Exception {

    // Constructor with error message only
    public InvalidInputException(String message) {
        super(message);
    }

    // Optional: Constructor allowing to wrap another exception
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
