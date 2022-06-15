package jobs;

import datastorage.DAOFactory;
import datastorage.PatientDAO;
import datastorage.TreatmentDAO;
import model.Patient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import utils.DateUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This cronjob allows the application to lock all users with an overdue Lock Mark Date
 */
public class PatientsLockJob implements Job {
    // according to the german dsgvo, patient data has to be kept 30 years long
    public static final int YEARS_UNTIL_DELETION = 30;

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
            patients = patientDAO.readAllWithLockDate();
            logger.log(Level.INFO, String.format("%d Patient/s with a Lock Date were found.", patients.size()));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Something went wrong while accessing the Database.");
            ex.printStackTrace();
            return;
        }

        patients.stream()
                .filter(patient -> new Date().after(patient.getLockDate()))
                .forEach(this::lockPatient);
    }

    /**
     * Locks the Patients and his Treatments
     *
     * @param patient The patient to be locked
     */
    private void lockPatient(Patient patient) {
        Date deletionDate = DateUtils.appendYearsToDate(new Date(), YEARS_UNTIL_DELETION);
        long pid = patient.getPid();
        logger.log(Level.INFO, "Patient was locked with the pid: " + pid);
        try {
            patientDAO.lockByPid(pid, deletionDate);
            treatmentDAO.lockByPid(pid);
        } catch (SQLException e) {
            logger.severe(
                    String.format("A exception occurred while locking the patient with the pid %s.", pid)
            );
            e.printStackTrace();
        }
    }
}
