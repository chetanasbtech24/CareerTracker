/**
 * Abstract domain root for people in the portal.
 *
 * OOP concept:
 * - Abstraction: this class defines shared state/behavior and requires
 *   subclasses to provide their own details representation.
 */
public abstract class Person {
    private String name;
    private String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract String getDetails();
}
