package enums;

/**
 * an enum for the groups that the application has
 */
public enum Group {
    ADMIN("Administrator/innen"),
    CAREGIVER("Pfleger/innen"),
    ACCOUNTANT("Buchhalter/innen");

    private String description;

    /**
     * constructor to set description for enum elements
     *
     * @param description the better definition of the elements
     */
    Group(String description) {
        this.description = description;
    }

    /**
     * toString()-method to recieve the description of an enum element
     */
    @Override
    public String toString() {
        return description;
    }
}
