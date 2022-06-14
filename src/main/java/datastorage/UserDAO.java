package datastorage;

import model.GroupFactory;
import model.User;
import utils.PasswordHashUtil;

import java.sql.*;
import java.util.ArrayList;

/**
 * DAO Class for the User<br>
 * Creates PreparedStatements for all DAO Operations for the User<br>
 * Implements the {@link DAOimp} interface
 */
public class UserDAO extends DAOimp<User> {

    public static final String USERS_TABLE_NAME = "users";

    public UserDAO(Connection conn) {
        super(conn);
    }

    /**
     * Statement for User Creation
     */
    @Override
    protected PreparedStatement getCreateStatement(User user) throws SQLException {
        PreparedStatement statement = getPreparedStatement(
                String.format("INSERT INTO %s ", USERS_TABLE_NAME) +
                "(surname, firstName, username, userGroup, password) VALUES (?, ?, ?, ?, ?)");
        fillPreparedStatement(user, statement);
        statement.setString(5, PasswordHashUtil.hashPassword(user.getPassword()));
        return statement;
    }

    /**
     * Statement to get the User by Id
     *
     * @param key uid of the user to read
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long key) throws SQLException {
        PreparedStatement statement = getPreparedStatement(
                String.format("SELECT * FROM %s WHERE uid = ?", USERS_TABLE_NAME)
        );
        statement.setLong(1, key);
        return statement;
    }

    /**
     * Statement to get the User by the Username
     *
     * @param username username of the user to read
     */
    private PreparedStatement getReadByUsernameStatement(String username) throws SQLException {
        PreparedStatement statement = getPreparedStatement(
                String.format("SELECT * FROM %s WHERE username = ?", USERS_TABLE_NAME)
        );
        statement.setString(1, username);
        return statement;
    }

    /**
     * executes statement that is produced from {@link #getReadByUsernameStatement(String)}
     */
    public User readByUsername(String username) throws SQLException {
        User object = null;
        ResultSet result = getReadByUsernameStatement(username).executeQuery();
        if (result.next()) {
            object = getInstanceFromResultSet(result);
        }
        return object;
    }

    /**
     * gets a user instance from a result set
     * @param set the ResultSet that contains the user object
     * @return the user object
     */
    @Override
    protected User getInstanceFromResultSet(ResultSet set) throws SQLException {
        return new User(set.getLong("uid"), set.getString("surname"),
                set.getString("firstName"), set.getString("username"),
                set.getString("password"), GroupFactory.getInstance().getGroup(set.getString("userGroup")));
    }

    /**
     * Statement to get all Users
     */
    @Override
    protected PreparedStatement getReadAllStatement() throws SQLException {
        return getPreparedStatement(String.format("SELECT * FROM %s", USERS_TABLE_NAME));
    }

    /**
     * Same thing like {@link #getInstanceFromResultSet(ResultSet)} but only with the intention
     * to create a list and return it
     */
    @Override
    protected ArrayList<User> getListFromResultSet(ResultSet set) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        while (set.next()) {
            users.add(new User(set.getLong("uid"), set.getString("surname"),
                    set.getString("firstName"), set.getString("username"),
                    set.getString("password"), GroupFactory.getInstance().getGroup(set.getString("userGroup"))));
        }
        return users;
    }

    /**
     * creates simple update statement<br>
     * the password is not included
     */
    @Override
    protected PreparedStatement getUpdateStatement(User user) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement(
                String.format("UPDATE %s SET ", USERS_TABLE_NAME) +
                "surname = ?, firstName = ?, username = ?, userGroup = ? WHERE uid = ?;");
        fillPreparedStatement(user, preparedStatement);
        preparedStatement.setLong(5, user.getUid());
        return preparedStatement;
    }

    /**
     * updates the password
     *
     * @param user user to fetch the id from
     * @param password the new password
     */
    public void updatePassword(User user, String password) throws SQLException {
        getUpdatePasswordStatement(user.getUid(), password).executeUpdate();
    }

    /**
     * creates a update statement to change the password of the user
     * @param uid uid of the user
     * @param password new password that replaces the old one
     */
    private PreparedStatement getUpdatePasswordStatement(long uid, String password) throws SQLException {
        PreparedStatement statement = getPreparedStatement(
                String.format("UPDATE %s SET password = ? WHERE uid = ?", USERS_TABLE_NAME)
        );
        statement.setString(1, PasswordHashUtil.hashPassword(password));
        statement.setLong(2, uid);
        return statement;
    }

    /**
     * delete statement to delete certain user entries
     * @param key the uid of the user
     */
    @Override
    protected PreparedStatement getDeleteStatement(long key) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement(String.format("DELETE FROM %s WHERE uid = ?",
                USERS_TABLE_NAME));
        preparedStatement.setLong(1, key);
        return preparedStatement;
    }

    /**
     * Fills the PreparedStatement and Hashes Password
     *
     * @param user user to fill into statement and validate password of
     * @param statement statement to fill
     * @throws SQLException throws if the a setString() Method of the Statement is out of index
     */
    private void fillPreparedStatement(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getSurname());
        statement.setString(2, user.getFirstName());
        statement.setString(3, user.getUsername());
        statement.setString(4, user.getGroup().getIdentifier());
    }
}
