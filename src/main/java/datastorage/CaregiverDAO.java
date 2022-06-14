package datastorage;

import model.Caregiver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implements the Interface <code>DAOImp</code>. Overrides methods to generate specific caregiver-SQL-queries.
 */
public class CaregiverDAO extends DAOimp<Caregiver>{
    /**
     * constructs Onbject. Calls the Constructor from <code>DAOImp</code> to store the connection.
     * @param conn
     */
    public CaregiverDAO(Connection conn) {
        super(conn);
    }

    /**
     * generates a <code>INSERT INTO</code>-Statement for a given caregiver
     * @param caregiver for which a specific INSERT INTO is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected PreparedStatement getCreateStatement(Caregiver caregiver) throws SQLException {
        PreparedStatement statement = getPreparedStatement("INSERT INTO caregiver (firstname, surname, telephone) VALUES (?, ?, ?)");
        fillPreparedStatement(caregiver, statement);
        return statement;
    }



    /**
     * generates a <code>select</code>-Statement for a given key
     * @param key for which a specific SELECT is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long key) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("SELECT * FROM caregiver WHERE cid = ?");
        preparedStatement.setLong(1, key);
        return preparedStatement;
    }

    /**
     * maps a <code>ResultSet</code> to a <code>Caregiver</code>
     * @param result ResultSet with a single row. Columns will be mapped to a caregiver-object.
     * @return patient with the data from the resultSet.
     */
    @Override
    protected Caregiver getInstanceFromResultSet(ResultSet result) throws SQLException {
        Caregiver c = null;
        c = new Caregiver(result.getInt(1), result.getString(2),
                result.getString(3), result.getString(4));
        return c;
    }

    /**
     * generates a <code>SELECT</code>-Statement for all patients.
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected PreparedStatement getReadAllStatement() throws SQLException {
        return getPreparedStatement("SELECT * FROM caregiver");
    }

    /**
     * maps a <code>ResultSet</code> to a <code>Caregiver-List</code>
     * @param result ResultSet with a multiple rows. Data will be mapped to caregiver-object.
     * @return ArrayList with caregivers from the resultSet.
     */
    @Override
    protected ArrayList<Caregiver> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Caregiver> list = new ArrayList<Caregiver>();
        Caregiver c = null;
        while (result.next()) {
            c = new Caregiver(result.getInt(1), result.getString(2),
                    result.getString(3), result.getString(4));
            list.add(c);
        }
        return list;
    }

    /**
     * generates a <code>UPDATE</code>-Statement for a given caregiver
     * @param caregiver for which a specific update is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected PreparedStatement getUpdateStatement(Caregiver caregiver) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("UPDATE caregiver SET firstname = ?, surname = ?, telephone = ?");
        fillPreparedStatement(caregiver, preparedStatement);
        return preparedStatement;
    }

    private void fillPreparedStatement(Caregiver caregiver, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, caregiver.getFirstName());
        preparedStatement.setString(2, caregiver.getSurname());
        preparedStatement.setString(3, caregiver.getTelephone());
    }

    /**
     * generates a <code>delete</code>-Statement for a given key
     * @param key for which a specific DELETE is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long key) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("DELETE FROM caregiver WHERE cid = ?");
        preparedStatement.setLong(1, key);
        return preparedStatement;
    }
}
