package model;

/**
 * A user to log in, log out and manage the Data in the Application
 */
public class User {
    private Long uid;
    private String lastName;
    private String firstName;
    private String username;
    private String password;

    public User() {
    }

    public User(String username, String password, String firstName, String lastName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.username = username;
        this.password = password;
    }

    public User(Long uid, String lastName, String firstName, String username, String password) {
        this.uid = uid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
                ", email='" + username + '\'' +
                '}';
    }
}
