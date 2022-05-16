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
        getCreateStatementString(t).executeUpdate();
    }

    @Override
    public T read(long key) throws SQLException {
        T object = null;
        Statement st = conn.createStatement();
        ResultSet result = getReadByIDStatementString(key).executeQuery();
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
        ResultSet result = getReadAllStatementString().executeQuery();
        list = getListFromResultSet(result);
        return list;
    }

    @Override
    public void update(T t) throws SQLException {
        getUpdateStatementString(t).executeUpdate();
    }

    @Override
    public void deleteById(long key) throws SQLException {
        getDeleteStatementString(key).executeUpdate();
    }

    protected PreparedStatement getPreparedStatement(String preparedStatementString) throws SQLException {
        return conn.prepareStatement(preparedStatementString);
    }
    protected abstract PreparedStatement getCreateStatementString(T t) throws SQLException;

    protected abstract PreparedStatement getReadByIDStatementString(long key) throws SQLException;

    protected abstract T getInstanceFromResultSet(ResultSet set) throws SQLException;

    protected abstract PreparedStatement getReadAllStatementString() throws SQLException;

    protected abstract ArrayList<T> getListFromResultSet(ResultSet set) throws SQLException;

    protected abstract PreparedStatement getUpdateStatementString(T t) throws SQLException;

    protected abstract PreparedStatement getDeleteStatementString(long key) throws SQLException;
}
