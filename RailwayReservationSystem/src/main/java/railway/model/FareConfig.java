package railway.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the dynamic fare multipliers for the application.
 * Accessible statically by Ticket classes.
 */
public class FareConfig {
    // Default multipliers (Fallbacks if file is missing)
    private static Map<String, Double> multipliers = new HashMap<>();

    static {
        // Initialize defaults exactly as they were in your hardcoded files
        multipliers.put("AC1", 3.5);
        multipliers.put("AC2", 2.7);
        multipliers.put("AC3", 2.2);
        multipliers.put("SLEEPER", 1.2);
        multipliers.put("GENERAL", 1.0);
    }

    public static double getMultiplier(String ticketClass) {
        return multipliers.getOrDefault(ticketClass.toUpperCase(), 1.0);
    }

    public static void setMultiplier(String ticketClass, double value) {
        multipliers.put(ticketClass.toUpperCase(), value);
    }

    public static Map<String, Double> getAllRules() {
        return multipliers;
    }

    public static void setAllRules(Map<String, Double> newRules) {
        if (newRules != null && !newRules.isEmpty()) {
            multipliers.putAll(newRules);
        }
    }
}