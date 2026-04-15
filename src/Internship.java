/**
 * Value object for internship experience owned by a Student.
 *
 * OOP concept:
 * - Encapsulation: internship state is private and accessible through
 *   getters/setters only.
 */
public class Internship {
    private String companyName;
    private int durationMonths;
    private String role;

    public Internship(String companyName, int durationMonths, String role) {
        this.companyName = companyName;
        this.durationMonths = durationMonths;
        this.role = role;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSummary() {
        return String.format(
                "  Company  : %s%n  Role     : %s%n  Duration : %d month(s)",
                companyName,
                role,
                durationMonths
        );
    }
}
