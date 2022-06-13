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

    @Override
    protected PreparedStatement getCreateStatement(User user) throws SQLException {
        PreparedStatement statement = getPreparedStatement(
                String.format("INSERT INTO %s ", USERS_TABLE_NAME) +
                "(surname, firstName, username, password, userGroup) VALUES (?, ?, ?, ?, ?)");
        fillPreparedStatement(user, statement);
        return statement;
    }

    @Override
    protected PreparedStatement getReadByIDStatement(long key) throws SQLException {
        PreparedStatement statement = getPreparedStatement(
                String.format("SELECT * FROM %s WHERE uid = ?", USERS_TABLE_NAME)
        );
        statement.setLong(1, key);
        return statement;
    }

    private PreparedStatement getReadByUsername(String username) throws SQLException {
        PreparedStatement statement = getPreparedStatement(
                String.format("SELECT * FROM %s WHERE username = ?", USERS_TABLE_NAME)
        );
        statement.setString(1, username);
        return statement;
    }

    public User readByUsername(String username) throws SQLException {
        User object = null;
        Statement st = conn.createStatement();
        ResultSet result = getReadByUsername(username).executeQuery();
        if (result.next()) {
            object = getInstanceFromResultSet(result);
        }
        return object;
    }

    @Override
    protected User getInstanceFromResultSet(ResultSet set) throws SQLException {
        return new User(set.getLong("uid"), set.getString("surname"),
                set.getString("firstName"), set.getString("username"),
                set.getString("password"), GroupFactory.getInstance().getGroup(set.getString("userGroup")));
    }

    @Override
    protected PreparedStatement getReadAllStatement() throws SQLException {
        return getPreparedStatement(String.format("SELECT * FROM %s", USERS_TABLE_NAME));
    }

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

    @Override
    protected PreparedStatement getUpdateStatement(User user) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement(
                String.format("UPDATE %s SET ", USERS_TABLE_NAME) +
                "surname = ?, firstName = ?, username = ?, password = ?, userGroup = ? WHERE uid = ?;");
        fillPreparedStatement(user, preparedStatement);
        preparedStatement.setLong(6, user.getUid());
        return preparedStatement;
    }

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
        statement.setString(4, PasswordHashUtil.hashPassword(user.getPassword()));
        statement.setString(5, user.getGroup().getIdentifier());
    }
}
