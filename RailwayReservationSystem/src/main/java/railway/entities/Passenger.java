package railway.entities;

/**
 * Passenger entity stores sensitive details for the person traveling.
 */
public class Passenger {
    private String name;
    private int age;
    private String idProof; // Example: Aadhaar, Passport, etc.

    public Passenger(String name, int age, String idProof) {
        this.name = name;
        this.age = age;
        this.idProof = idProof;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getIdProof() {
        return idProof;
    }
    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }
}
