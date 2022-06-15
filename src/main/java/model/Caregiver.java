package model;

/**
 * Caregivers are treating the Patients.
 */
public class Caregiver extends Person {
    private long cid;
    private String telephone;

    /**
     * constructs a patient from the given params.
     * @param firstName
     * @param surname
     * @param telephone
     */
    public Caregiver(String firstName, String surname, String telephone) {
        super(firstName, surname);
        this.telephone = telephone;
    }

    /**
     * constructs a caregiver from the given params.
     * @param cid
     * @param firstName
     * @param surname
     * @param telephone
     */
    public Caregiver(long cid, String firstName, String surname, String telephone) {
        super(firstName, surname);
        this.cid = cid;
        this.telephone = telephone;
        }

    /**
     *
     * @return caregiver id
     */
    public long getCid() {
        return cid;
    }

    /**
     *
     * @return telephone as a string
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone as string
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     *
     * @return string-representation of the patient
     */
    public String toString() {
        return "Patient" + "\nMNID: " + this.cid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nTelephone: " + this.telephone +
                "\n";
    }
}
