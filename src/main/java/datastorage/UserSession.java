package datastorage;

import model.User;

/**
 * Singleton for <code>User</code> in order to keep the logged-in user stored
 */
public final class UserSession {

    private static UserSession instance;

    private User user;

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void init(User user) {
        this.user = user;
    }

    /**
     * Clears the Singleton
     */
    public void clear() {
        // sets the singleton and the data of it to null
        // by doing this, the gc will collect the data
        user = null;
        instance = null;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public long getUid() {
        return user.getUid();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }
}
