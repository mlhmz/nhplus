package jobs;

import datastorage.DAOFactory;
import datastorage.PatientDAO;
import datastorage.TreatmentDAO;
import model.Patient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This cronjob allows the application to lock all users with an overdue Deletion Date
 */
public class PatientsDeleteJob implements Job {
    private PatientDAO patientDAO;
    private TreatmentDAO treatmentDAO;

    private Logger logger;

    /**
     * Executes the Cronjob, Starting Point
     *
     * @param jobExecutionContext Context of the Execution
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logger = Logger.getLogger(jobExecutionContext.getJobDetail().getKey().toString());
        logger.log(Level.INFO, "Executing the Job");

        patientDAO = DAOFactory.getDAOFactory().createPatientDAO();
        treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();

        // Gets all Patients with LockDate
        List<Patient> patients;
        try {
            patients = patientDAO.readAllLocked();
            logger.log(Level.INFO, String.format("%d locked Patient/s were found.", patients.size()));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Something went wrong while accessing the Database.");
            ex.printStackTrace();
            return;
        }

        patients.stream()
                .filter(patient -> new Date().after(patient.getDeletionDate()))
                .forEach(this::deletePatient);
    }

    /**
     * Locks the Patients and his Treatments
     *
     * @param patient The patient to be locked
     */
    private void deletePatient(Patient patient) {
        long pid = patient.getPid();
        try {
            treatmentDAO.deleteByPid(pid);
            patientDAO.deleteById(pid);
        } catch (SQLException e) {
            logger.severe(
                    String.format("A exception occurred while locking the patient with the pid %s.", pid)
            );
            e.printStackTrace();
        }
        logger.log(Level.INFO, "Patient was deleted with the pid: " + pid);
    }
}
