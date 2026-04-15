import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Student domain entity.
 *
 * OOP concepts:
 * - Inheritance: Student extends Person.
 * - Composition: Student has an Internship.
 * - Polymorphism: Student overrides getDetails().
 * - Encapsulation: mutable collections are not exposed directly.
 */
public class Student extends Person {
    public static class SkillRating {
        private final String skill;
        private final int rating;

        public SkillRating(String skill, int rating) {
            this.skill = skill;
            this.rating = Math.max(0, Math.min(5, rating));
        }

        public String getSkill() {
            return skill;
        }

        public int getRating() {
            return rating;
        }

        @Override
        public String toString() {
            return rating > 0 ? String.format("%s (%d/5)", skill, rating) : skill;
        }
    }

    private String usn;
    private final List<SkillRating> skills;
    private Internship internship;

    public Student(String name, String email, String usn) {
        super(name, email);
        this.usn = usn;
        this.skills = new ArrayList<>();
        this.internship = null;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    /**
     * Returns a read-only view to protect internal collection state.
     */
    public List<SkillRating> getSkills() {
        return Collections.unmodifiableList(skills);
    }

    public List<String> getSkillLabels() {
        List<String> labels = new ArrayList<>();
        for (SkillRating item : skills) {
            labels.add(item.toString());
        }
        return Collections.unmodifiableList(labels);
    }

    public Internship getInternship() {
        return internship;
    }

    public void setInternship(Internship internship) {
        this.internship = internship;
    }

    public void addSkill(String skill) {
        addSkill(skill, 0);
    }

    public void addSkill(String skill, int rating) {
        if (skill != null && !skill.trim().isEmpty()) {
            skills.add(new SkillRating(skill.trim(), rating));
        }
    }

    @Override
    public String getDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name       : ").append(getName()).append("\n");
        sb.append("Email      : ").append(getEmail()).append("\n");
        sb.append("ID         : ").append(usn).append("\n");
        sb.append("Skills     : ")
                .append(skills.isEmpty() ? "None yet" : String.join(", ", getSkillLabels()))
                .append("\n");
        sb.append("Experience : ");
        if (internship == null) {
            sb.append("Not added yet");
        } else {
            sb.append("\n").append(internship.getSummary());
        }
        return sb.toString();
    }
}
