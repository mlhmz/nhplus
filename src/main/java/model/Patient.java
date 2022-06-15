package model;

import utils.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Patients live in a NURSING home and are treated by nurses.
 */
public class Patient extends Person {
    private long pid;
    private LocalDate dateOfBirth;
    private String careLevel;
    private String roomnumber;
    private Date lockDate;
    private Date deletionDate;
    private boolean locked;
    private List<Treatment> allTreatments = new ArrayList<Treatment>();

    /**
     * constructs a patient from the given params.
     * @param firstName
     * @param surname
     * @param dateOfBirth
     * @param careLevel
     * @param roomnumber
     */
    public Patient(String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomnumber) {
        super(firstName, surname);
        this.dateOfBirth = dateOfBirth;
        this.careLevel = careLevel;
        this.roomnumber = roomnumber;
    }

    /**
     * constructs a patient from the given params.
     * @param firstName
     * @param surname
     * @param dateOfBirth
     * @param careLevel
     * @param roomnumber
     * @param lockDate date when user will be locked
     */
    public Patient(String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomnumber,
                   Date lockDate) {
        super(firstName, surname);
        this.dateOfBirth = dateOfBirth;
        this.careLevel = careLevel;
        this.roomnumber = roomnumber;
        this.lockDate = lockDate;
    }

    /**
     * constructs a patient from the given params.
     * @param firstName
     * @param surname
     * @param dateOfBirth
     * @param careLevel
     * @param roomnumber
     * @param lockDate date when user will be locked
     * @param deletionDate date when user will be deleted, when the patient is locked
     *                     it also has to 100% a deletion date
     * @param locked boolean if patient is locked, most probably when the lockDate already expired
     */
    public Patient(String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomnumber,
                   Date lockDate, Date deletionDate, boolean locked) {
        super(firstName, surname);
        this.dateOfBirth = dateOfBirth;
        this.careLevel = careLevel;
        this.roomnumber = roomnumber;
        this.lockDate = lockDate;
        this.deletionDate = deletionDate;
        this.locked = locked;
    }

    /**
     * constructs a patient from the given params.
     * @param pid
     * @param firstName
     * @param surname
     * @param dateOfBirth
     * @param careLevel
     * @param roomnumber
     * @param lockDate date when user will be locked
     * @param deletionDate date when user will be deleted, when the patient is locked
     *                     it also has to 100% a deletion date
     * @param locked boolean if patient is locked, most probably when the lockDate already expired
     */
    public Patient(long pid, String firstName, String surname, LocalDate dateOfBirth, String careLevel,
                   String roomnumber, Date lockDate, Date deletionDate, boolean locked) {
        super(firstName, surname);
        this.pid = pid;
        this.dateOfBirth = dateOfBirth;
        this.careLevel = careLevel;
        this.roomnumber = roomnumber;
        this.lockDate = lockDate;
        this.deletionDate = deletionDate;
        this.locked = locked;
    }

    /**
     *
     * @return patient id
     */
    public long getPid() {
        return pid;
    }

    /**
     *
     * @return date of birth as a string
     */
    public String getDateOfBirth() {
        return dateOfBirth.toString();
    }

    /**
     * convert given param to a localDate and store as new <code>birthOfDate</code>
     * @param dateOfBirth as string in the following format: YYYY-MM-DD
     */
    public void setDateOfBirth(String dateOfBirth) {
        LocalDate birthday = DateUtils.convertStringToLocalDate(dateOfBirth);
        this.dateOfBirth = birthday;
    }

    /**
     *
     * @return careLevel
     */
    public String getCareLevel() {
        return careLevel;
    }

    /**
     *
     * @param careLevel new care level
     */
    public void setCareLevel(String careLevel) {
        this.careLevel = careLevel;
    }

    /**
     *
     * @return roomNumber as string
     */
    public String getRoomnumber() {
        return roomnumber;
    }

    /**
     *
     * @param roomnumber
     */
    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    /**
     * gets the <code>lockDate</code> of the Patient
     *
     * @return the <code>lockDate</code> of the <code>Patient</code>
     */
    public Date getLockDate() {
        return lockDate;
    }

    /**
     * adds a treatment to the treatment-list, if it does not already contain it.
     * @param m Treatment
     * @return true if the treatment was not already part of the list. otherwise false
     */
    public boolean add(Treatment m) {
        if (!this.allTreatments.contains(m)) {
            this.allTreatments.add(m);
            return true;
        }
        return false;
    }

    /**
     *
     * @return string-representation of the patient
     */
    public String toString() {
        return "Patient" + "\nMNID: " + this.pid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nBirthday: " + this.dateOfBirth +
                "\nCarelevel: " + this.careLevel +
                "\nRoomnumber: " + this.roomnumber +
                "\n";
    }
}