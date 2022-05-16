package datastorage;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO extends DAOimp<User> {
    public UserDAO(Connection conn) {
        super(conn);
    }

    @Override
    protected PreparedStatement getCreateStatementString(User user) throws SQLException {
        PreparedStatement statement = getPreparedStatement("INSERT INTO user " +
                "(lastName, firstName, email, password) VALUES (?, ?, ?, ?)");
        fillPreparedStatement(user, statement);
        return statement;
    }

    @Override
    protected PreparedStatement getReadByIDStatementString(long key) throws SQLException {
        PreparedStatement statement = getPreparedStatement("SELECT * FROM user WHERE uid = ?");
        statement.setLong(1, key);
        return statement;
    }

    @Override
    protected User getInstanceFromResultSet(ResultSet set) throws SQLException {
        return new User(set.getLong("uid"), set.getString("lastName"),
                set.getString("firstName"), set.getString("email"),
                set.getString("password"));
    }

    @Override
    protected PreparedStatement getReadAllStatementString() throws SQLException {
        return getPreparedStatement("SELECT * FROM user");
    }

    @Override
    protected ArrayList<User> getListFromResultSet(ResultSet set) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        while (set.next()) {
            users.add(new User(set.getLong("uid"), set.getString("lastName"),
                    set.getString("firstName"), set.getString("email"),
                    set.getString("password")));
        }
        return users;
    }

    @Override
    protected PreparedStatement getUpdateStatementString(User user) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("UPDATE user SET lastName = ?, firstName = ?, email = ?, password = ? WHERE uid = ?,");
        fillPreparedStatement(user, preparedStatement);
        preparedStatement.setLong(5, user.getUid());
        return preparedStatement;
    }

    @Override
    protected PreparedStatement getDeleteStatementString(long key) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("DELETE FROM user WHERE uid = ?");
        preparedStatement.setLong(1, key);
        return preparedStatement;
    }

    private void fillPreparedStatement(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getLastName());
        statement.setString(2, user.getFirstName());
        statement.setString(3, user.getLastName());
        statement.setString(4, user.getPassword());
    }
}
