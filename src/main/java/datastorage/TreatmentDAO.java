package datastorage;

import model.Treatment;
import utils.DateConverter;

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
        PreparedStatement preparedStatement = getPreparedStatement("INSERT INTO treatment (pid, treatment_date, begin, end, description, remarks) VALUES (?, ?, ?, ?, ?, ?);");
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
        LocalDate date = DateConverter.convertStringToLocalDate(result.getString(3));
        LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(4));
        LocalTime end = DateConverter.convertStringToLocalTime(result.getString(5));
        Treatment m = new Treatment(result.getLong(1), result.getLong(2),
                date, begin, end, result.getString(6), result.getString(7));
        return m;
    }

    @Override
    protected PreparedStatement getReadAllStatement() throws SQLException {
        return getPreparedStatement("SELECT * FROM treatment");
    }

    @Override
    protected ArrayList<Treatment> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment t = null;
        while (result.next()) {
            LocalDate date = DateConverter.convertStringToLocalDate(result.getString(3));
            LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(4));
            LocalTime end = DateConverter.convertStringToLocalTime(result.getString(5));
            t = new Treatment(result.getLong(1), result.getLong(2),
                    date, begin, end, result.getString(6), result.getString(7));
            list.add(t);
        }
        return list;
    }

    @Override
    protected PreparedStatement getUpdateStatement(Treatment treatment) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("UPDATE treatment SET pid = ?, treatment_date = ?, begin = ?, end = ?, description = ?, remarks = ? WHERE tid = ?");
        fillPreparedStatement(treatment, preparedStatement);
        preparedStatement.setLong(7, treatment.getTid());
        return preparedStatement;
    }

    private void fillPreparedStatement(Treatment treatment, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, treatment.getPid());
        preparedStatement.setString(2, treatment.getDate());
        preparedStatement.setString(3, treatment.getBegin());
        preparedStatement.setString(4, treatment.getEnd());
        preparedStatement.setString(5, treatment.getDescription());
        preparedStatement.setString(6, treatment.getRemarks());
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

    public void deleteByPid(long key) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement("Delete FROM treatment WHERE pid = ?");
        preparedStatement.setLong(1, key);
    }
}