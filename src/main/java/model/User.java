package model;

/**
 * A user to log in, log out and manage the Data in the Application
 */
public class User extends Person {
    private Long uid;
    private String username;
    private String password;
    private Group group;

    /**
     * Constructor with uid for User
     *
     * @param surname the users surname
     * @param firstName the users firstname
     * @param username the users username
     * @param password the users password, will be hashed in database
     * @param group the users group
     */
    public User(String username, String password, String firstName, String surname, Group group) {
        super(firstName, surname);
        this.username = username;
        this.password = password;
        this.group = group;
    }

    /**
     * Constructor with uid for User
     *
     * @param uid the user id
     * @param surname the users surname
     * @param firstName the users firstname
     * @param username the users username
     * @param password the users password, will be hashed in database
     * @param group the users group
     */
    public User(Long uid, String surname, String firstName, String username, String password, Group group) {
        super(firstName, surname);
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.group = group;
    }

    /**
     * gets the users uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * sets uid
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * gets username
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * gets password
     */
    public String getPassword() {
        return password;
    }

    /**
     * sets password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * gets group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * sets group
     */
    public void setGroup(Group group) {
        this.group = group;
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
                ", lastName='" + getSurname() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", email='" + username + '\'' +
                '}';
    }
}
