package datastorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DAOimp<T> implements DAO<T>{
    protected Connection conn;

    public DAOimp(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(T t) throws SQLException {
        getCreateStatement(t).executeUpdate();
    }

    @Override
    public T read(long key) throws SQLException {
        T object = null;
        Statement st = conn.createStatement();
        ResultSet result = getReadByIDStatement(key).executeQuery();
        if (result.next()) {
            object = getInstanceFromResultSet(result);
        }
        return object;
    }

    @Override
    public List<T> readAll() throws SQLException {
        ArrayList<T> list = new ArrayList<T>();
        T object = null;
        Statement st = conn.createStatement();
        ResultSet result = getReadAllStatement().executeQuery();
        list = getListFromResultSet(result);
        return list;
    }

    @Override
    public void update(T t) throws SQLException {
        getUpdateStatement(t).executeUpdate();
    }

    @Override
    public void deleteById(long key) throws SQLException {
        getDeleteStatement(key).executeUpdate();
    }

    protected PreparedStatement getPreparedStatement(String preparedStatementString) throws SQLException {
        return conn.prepareStatement(preparedStatementString);
    }
    protected abstract PreparedStatement getCreateStatement(T t) throws SQLException;

    protected abstract PreparedStatement getReadByIDStatement(long key) throws SQLException;

    protected abstract T getInstanceFromResultSet(ResultSet set) throws SQLException;

    protected abstract PreparedStatement getReadAllStatement() throws SQLException;

    protected abstract ArrayList<T> getListFromResultSet(ResultSet set) throws SQLException;

    protected abstract PreparedStatement getUpdateStatement(T t) throws SQLException;

    protected abstract PreparedStatement getDeleteStatement(long key) throws SQLException;
}
