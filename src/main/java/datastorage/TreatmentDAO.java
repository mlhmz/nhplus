package datastorage;

import model.Treatment;
import utils.DateUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO extends DAOimp<Treatment> {

    public TreatmentDAO(Connection conn) {
        super(conn);
    }

    @Override
    protected PreparedStatement getCreateStatement(Treatment treatment) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("INSERT INTO treatment (pid, treatment_date, begin, end, description, remarks, cid) VALUES (?, ?, ?, ?, ?, ?, ?);");
        fillPreparedStatement(treatment, preparedStatement);
        return preparedStatement;
    }

    @Override
    protected PreparedStatement getReadByIDStatement(long key) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("SELECT * FROM treatment WHERE tid = ?");
        preparedStatement.setLong(1, key);
        return preparedStatement;
    }

    @Override
    protected Treatment getInstanceFromResultSet(ResultSet result) throws SQLException {
        LocalDate date = DateUtils.convertStringToLocalDate(result.getString(3));
        LocalTime begin = DateUtils.convertStringToLocalTime(result.getString(4));
        LocalTime end = DateUtils.convertStringToLocalTime(result.getString(5));
        return new Treatment(result.getLong(1), result.getLong(2), result.getLong(8),
                date, begin, end, result.getString(6), result.getString(7), result.getBoolean(9));
    }

    /**
     * statement to get all treatments except locked ones
     *
     * @return all treatments
     * @throws SQLException if something went wrong with the database
     */
    @Override
    protected PreparedStatement getReadAllStatement() throws SQLException {
        PreparedStatement preparedStatement =
                getPreparedStatement("SELECT * FROM treatment WHERE locked = ?");
        preparedStatement.setBoolean(1, false);
        return preparedStatement;
    }

    @Override
    protected ArrayList<Treatment> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment t = null;
        while (result.next()) {
            LocalDate date = DateUtils.convertStringToLocalDate(result.getString(3));
            LocalTime begin = DateUtils.convertStringToLocalTime(result.getString(4));
            LocalTime end = DateUtils.convertStringToLocalTime(result.getString(5));
            t = new Treatment(result.getLong(1), result.getLong(2), result.getLong(8),
                    date, begin, end, result.getString(6), result.getString(7),
                    result.getBoolean(9));
            list.add(t);
        }
        return list;
    }

    @Override
    protected PreparedStatement getUpdateStatement(Treatment treatment) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("UPDATE treatment SET pid = ?, treatment_date = ?, begin = ?, end = ?, description = ?, remarks = ?, cid = ? WHERE tid = ?");
        fillPreparedStatement(treatment, preparedStatement);
        preparedStatement.setLong(8, treatment.getTid());
        return preparedStatement;
    }

    private void fillPreparedStatement(Treatment treatment, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, treatment.getPid());
        preparedStatement.setString(2, treatment.getDate());
        preparedStatement.setString(3, treatment.getBegin());
        preparedStatement.setString(4, treatment.getEnd());
        preparedStatement.setString(5, treatment.getDescription());
        preparedStatement.setString(6, treatment.getRemarks());
        preparedStatement.setLong(7, treatment.getCid());
    }

    @Override
    protected PreparedStatement getDeleteStatement(long key) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("DELETE FROM treatment WHERE tid = ?");
        preparedStatement.setLong(1, key);
        return preparedStatement;
    }

    public List<Treatment> readTreatmentsByPid(long pid) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment object = null;
        Statement st = conn.createStatement();
        ResultSet result = getReadAllTreatmentsOfOnePatientByPid(pid).executeQuery();
        list = getListFromResultSet(result);
        return list;
    }

    private PreparedStatement getReadAllTreatmentsOfOnePatientByPid(long pid) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("SELECT * FROM treatment WHERE pid = ?");
        preparedStatement.setLong(1, pid);
        return preparedStatement;
    }

    /**
     * Locks all Treatments that have a certain Patient
     *
     * @param pid the patients id
     * @throws SQLException if something went wrong in the database
     */
    public void lockByPid(long pid) throws SQLException {
        getLockByPidStatement(pid).executeUpdate();
    }

    /**
     * Statement to Lock all Treatments that have a certain Patient
     *
     * @param pid the patients id
     * @throws SQLException if something went wrong in the database
     */
    public PreparedStatement getLockByPidStatement(long pid) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement(
                "UPDATE treatment SET locked = ? WHERE pid = ?"
        );
        preparedStatement.setBoolean(1, true);
        preparedStatement.setLong(2, pid);
        return preparedStatement;
    }

    public void deleteByPid(long key) throws SQLException {
        getDeleteByPidStatement(key).executeUpdate();
    }

    public PreparedStatement getDeleteByPidStatement(long key) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("DELETE FROM treatment WHERE pid = ?");
        preparedStatement.setLong(1, key);
        return preparedStatement;
    }
}