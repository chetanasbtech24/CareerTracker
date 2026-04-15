import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple JDBC helper for SQLite persistence.
 *
 * Stores:
 * - students table (core profile + internship columns)
 * - skills table (one row per skill)
 */
public class DatabaseManager {
    private final String jdbcUrl;

    public DatabaseManager(String dbFileName) {
        this.jdbcUrl = "jdbc:sqlite:" + dbFileName;
    }

    public void initializeSchema() {
        String createStudents = """
                CREATE TABLE IF NOT EXISTS students (
                    usn TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL,
                    company_name TEXT,
                    internship_role TEXT,
                    internship_duration INTEGER
                )
                """;

        String createSkills = """
                CREATE TABLE IF NOT EXISTS skills (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_usn TEXT NOT NULL,
                    skill TEXT NOT NULL,
                    rating INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY(student_usn) REFERENCES students(usn) ON DELETE CASCADE
                )
                """;

        try (Connection conn = openConnection();
             Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
            st.execute(createStudents);
            st.execute(createSkills);
            addRatingColumnIfMissing(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize SQLite schema", e);
        }
    }

    public List<Student> loadAllStudents() {
        String sql = """
                SELECT s.usn, s.name, s.email, s.company_name, s.internship_role, s.internship_duration, k.skill, k.rating
                FROM students s
                LEFT JOIN skills k ON k.student_usn = s.usn
                ORDER BY s.usn, k.id
                """;

        Map<String, Student> byUsn = new LinkedHashMap<>();
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String usn = rs.getString("usn");
                Student student = byUsn.get(usn);
                if (student == null) {
                    student = new Student(rs.getString("name"), rs.getString("email"), usn);

                    String company = rs.getString("company_name");
                    String role = rs.getString("internship_role");
                    int months = rs.getInt("internship_duration");
                    boolean hasInternship = company != null && role != null && !rs.wasNull();
                    if (hasInternship) {
                        student.setInternship(new Internship(company, months, role));
                    }

                    byUsn.put(usn, student);
                }

                String skill = rs.getString("skill");
                int rating = rs.getInt("rating");
                if (skill != null && !skill.isBlank()) {
                    student.addSkill(skill, rating);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load students from SQLite", e);
        }
        return new ArrayList<>(byUsn.values());
    }

    public void insertStudent(Student student) {
        String sql = """
                INSERT INTO students (usn, name, email, company_name, internship_role, internship_duration)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getUsn());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());

            Internship internship = student.getInternship();
            if (internship == null) {
                ps.setString(4, null);
                ps.setString(5, null);
                ps.setObject(6, null);
            } else {
                ps.setString(4, internship.getCompanyName());
                ps.setString(5, internship.getRole());
                ps.setInt(6, internship.getDurationMonths());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert student into SQLite", e);
        }
    }

    public void insertSkill(String usn, String skill, int rating) {
        String sql = "INSERT INTO skills (student_usn, skill, rating) VALUES (?, ?, ?)";
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usn);
            ps.setString(2, skill);
            ps.setInt(3, Math.max(0, Math.min(5, rating)));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert skill into SQLite", e);
        }
    }

    private void addRatingColumnIfMissing(Connection conn) {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("PRAGMA table_info(skills)")) {
            boolean hasRating = false;
            while (rs.next()) {
                if ("rating".equalsIgnoreCase(rs.getString("name"))) {
                    hasRating = true;
                    break;
                }
            }
            if (!hasRating) {
                st.execute("ALTER TABLE skills ADD COLUMN rating INTEGER NOT NULL DEFAULT 0");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update skills table schema", e);
        }
    }

    public void updateInternship(String usn, Internship internship) {
        String sql = """
                UPDATE students
                SET company_name = ?, internship_role = ?, internship_duration = ?
                WHERE usn = ?
                """;
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, internship.getCompanyName());
            ps.setString(2, internship.getRole());
            ps.setInt(3, internship.getDurationMonths());
            ps.setString(4, usn);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update internship in SQLite", e);
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }
}
