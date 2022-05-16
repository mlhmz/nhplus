package model;

/**
 * A user to log in, log out and manage the Data in the Application
 *
 */
public class User {
    private Long uid;
    private String lastName;
    private String firstName;
    private String email;
    private String password;

    public User() {
    }

    public User(Long uid, String lastName, String firstName, String email, String password) {
        this.uid = uid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Converts the User to a String without the Password
     *
     * @return User as String without the Password
     */
    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
