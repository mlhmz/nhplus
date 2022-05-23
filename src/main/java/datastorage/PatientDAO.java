package datastorage;

import model.Patient;
import utils.DateConverter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Implements the Interface <code>DAOImp</code>. Overrides methods to generate specific patient-SQL-queries.
 */
public class PatientDAO extends DAOimp<Patient> {

    /**
     * constructs Onbject. Calls the Constructor from <code>DAOImp</code> to store the connection.
     * @param conn
     */
    public PatientDAO(Connection conn) {
        super(conn);
    }

    /**
     * generates a <code>INSERT INTO</code>-Statement for a given patient
     * @param patient for which a specific INSERT INTO is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected PreparedStatement getCreateStatement(Patient patient) throws SQLException {
        PreparedStatement statement = getPreparedStatement("INSERT INTO patient (firstname, surname, dateOfBirth, carelevel, roomnumber) VALUES (?, ?, ?, ?, ?)");
        fillPreparedStatement(patient, statement);
        return statement;
    }



    /**
     * generates a <code>select</code>-Statement for a given key
     * @param key for which a specific SELECTis to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long key) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("SELECT * FROM patient WHERE pid = ?");
        preparedStatement.setLong(1, key);
        return preparedStatement;
    }

    /**
     * maps a <code>ResultSet</code> to a <code>Patient</code>
     * @param result ResultSet with a single row. Columns will be mapped to a patient-object.
     * @return patient with the data from the resultSet.
     */
    @Override
    protected Patient getInstanceFromResultSet(ResultSet result) throws SQLException {
        Patient p = null;
        LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
        p = new Patient(result.getInt(1), result.getString(2),
                result.getString(3), date, result.getString(5),
                result.getString(6));
        return p;
    }

    /**
     * generates a <code>SELECT</code>-Statement for all patients.
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected PreparedStatement getReadAllStatement() throws SQLException {
        return getPreparedStatement("SELECT * FROM patient");
    }

    /**
     * maps a <code>ResultSet</code> to a <code>Patient-List</code>
     * @param result ResultSet with a multiple rows. Data will be mapped to patient-object.
     * @return ArrayList with patients from the resultSet.
     */
    @Override
    protected ArrayList<Patient> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Patient> list = new ArrayList<Patient>();
        Patient p = null;
        while (result.next()) {
            LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
            p = new Patient(result.getInt(1), result.getString(2),
                    result.getString(3), date,
                    result.getString(5), result.getString(6));
            list.add(p);
        }
        return list;
    }

    /**
     * generates a <code>UPDATE</code>-Statement for a given patient
     * @param patient for which a specific update is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected PreparedStatement getUpdateStatement(Patient patient) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("UPDATE patient SET firstname = ?, surname = ?, dateOfBirth = ?, carelevel = ?, roomnumber = ?");
        fillPreparedStatement(patient, preparedStatement);
        return preparedStatement;
    }

    private void fillPreparedStatement(Patient patient, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, patient.getFirstName());
        preparedStatement.setString(2, patient.getSurname());
        preparedStatement.setString(3, patient.getDateOfBirth());
        preparedStatement.setString(4, patient.getCareLevel());
        preparedStatement.setString(5, patient.getRoomnumber());
    }

    /**
     * generates a <code>delete</code>-Statement for a given key
     * @param key for which a specific DELETE is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long key) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("Delete FROM patient WHERE pid = ?");
        preparedStatement.setLong(1, key);
        return preparedStatement;
    }
}
