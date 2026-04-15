import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Business/service layer for student profile operations.
 *
 * OOP concept:
 * - Encapsulation of business data access: UI talks to this manager instead
 *   of directly owning/modifying storage collections.
 */
public class PortalManager {
    private final List<Student> studentList = new ArrayList<>();
    private final DatabaseManager databaseManager;
    private final boolean dbEnabled;

    public PortalManager() {
        DatabaseManager manager = null;
        boolean enabled = false;
        try {
            manager = new DatabaseManager("career_tracker.db");
            manager.initializeSchema();
            studentList.addAll(manager.loadAllStudents());
            enabled = true;
        } catch (RuntimeException ex) {
            // Keep app usable even if DB setup fails.
            System.err.println("SQLite unavailable. Running in-memory only: " + ex.getMessage());
        }
        this.databaseManager = manager;
        this.dbEnabled = enabled;
    }

    public void addStudent(Student student) {
        studentList.add(student);
        if (dbEnabled) {
            databaseManager.insertStudent(student);
        }
    }

    /**
     * Returns a read-only view to prevent direct UI mutation.
     */
    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(studentList);
    }

    public Student findByUsn(String usn) {
        for (Student student : studentList) {
            if (student.getUsn().equalsIgnoreCase(usn)) {
                return student;
            }
        }
        return null;
    }

    public boolean usnExists(String usn) {
        return findByUsn(usn) != null;
    }

    public void addSkill(String usn, String skill) {
        addSkill(usn, skill, 0);
    }

    public void addSkill(String usn, String skill, int rating) {
        Student student = findByUsn(usn);
        if (student == null) {
            return;
        }
        student.addSkill(skill, rating);
        if (dbEnabled) {
            databaseManager.insertSkill(usn, skill, rating);
        }
    }

    public void assignInternship(String usn, Internship internship) {
        Student student = findByUsn(usn);
        if (student == null) {
            return;
        }
        student.setInternship(internship);
        if (dbEnabled) {
            databaseManager.updateInternship(usn, internship);
        }
    }

    // Method overloading: convenience overload that builds an Internship object.
    public void assignInternship(String usn, String companyName, int durationMonths, String role) {
        assignInternship(usn, new Internship(companyName, durationMonths, role));
    }
}
