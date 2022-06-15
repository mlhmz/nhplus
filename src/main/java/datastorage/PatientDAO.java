package datastorage;

import model.Patient;
import utils.DateUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

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
        LocalDate date = DateUtils.convertStringToLocalDate(result.getString(4));
        p = new Patient(result.getInt(1), result.getString(2),
                result.getString(3), date, result.getString(5),
                result.getString(6),
                DateUtils.convertCompleteDateStringToString(result.getString(7)));
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
            LocalDate date = DateUtils.convertStringToLocalDate(result.getString(4));
            p = new Patient(result.getInt(1), result.getString(2),
                    result.getString(3), date,
                    result.getString(5), result.getString(6),
                    DateUtils.convertCompleteDateStringToString(result.getString(7)));
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
        PreparedStatement preparedStatement = getPreparedStatement(
                "UPDATE patient SET firstname = ?, surname = ?, dateOfBirth = ?, carelevel = ?, roomnumber = ? WHERE pid = ?");
        fillPreparedStatement(patient, preparedStatement);
        preparedStatement.setLong(6, patient.getPid());
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
     * Marks the Patient with his pid
     *
     * @param pid the identification of the patient
     */
    public void markLockByPid(long pid, Date lockDate) throws SQLException {
        getLockMarkStatement(pid, lockDate).executeUpdate();
    }

    /**
     * Creates a statement for locking the patients
     *
     * @param pid the patients id
     * @param lockDate the date when the patient gets locked
     * @return {@link PreparedStatement} filled with the content
     * @throws SQLException thrown when something went wrong with the PreparedStatement e.g. out of index
     */
    private PreparedStatement getLockMarkStatement(long pid, Date lockDate) throws SQLException {
        String dateString = DateUtils.convertCompleteDateToString(lockDate);
        PreparedStatement preparedStatement = getPreparedStatement(
                "UPDATE patient SET lockDate = ? WHERE pid = ?"
        );
        preparedStatement.setString(1, dateString);
        preparedStatement.setLong(2, pid);
        return preparedStatement;
    }

    /**
     * Removes the Mark from the Patient with his <code>pid</code>
     *
     * @param pid the identification of the patient
     */
    public void removeLockMarkByPid(long pid) throws SQLException {
        getRemoveLockMarkStatement(pid).executeUpdate();
    }

    /**
     * Statement to remove Lock Mark from the User
     *
     * @param pid the identification of the user
     * @return {@link PreparedStatement} which sets the <code>lockDate</code>of the <code>pid</code> param to null
     * @throws SQLException thrown when something went wrong with the PreparedStatement e.g. out of index
     */
    private PreparedStatement getRemoveLockMarkStatement(long pid) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement(
                "UPDATE patient SET lockDate = NULL WHERE pid = ?"
        );
        preparedStatement.setLong(1, pid);
        return preparedStatement;
    }

    /**
     * generates a <code>delete</code>-Statement for a given key
     * @param key for which a specific DELETE is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long key) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("DELETE FROM patient WHERE pid = ?");
        preparedStatement.setLong(1, key);
        return preparedStatement;
    }
}
