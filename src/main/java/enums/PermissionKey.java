package enums;

/**
 * Enum for the Controller-View-Access, is assigned to Groups {@link model.Group}
 */
public enum PermissionKey {
    /**
     * The MainWindow of the Application (see {@link controller.MainWindowController}),
     * usually, every group should recieve this permission
     */
    SHOW_HOMEPAGE("Startseite zeigen"),
    /**
     * Permission to open the Patient Table (see {@link controller.AllPatientController})
     */
    SHOW_ALL_PATIENTS("Patienten zeigen"),
    /**
     * Permission to the Patient Creation in the {@link controller.AllPatientController}
     */
    CREATE_PATIENT("Patient erstellen"),
    /**
     * Permission to the Patient Editing in the {@link controller.AllPatientController}
     */
    EDIT_PATIENT("Patient editieren"),
    /**
     * Permission to the Patient Deletion in the {@link controller.AllPatientController}
     */
    DELETE_PATIENT("Patient löschen"),
    /**
     * Permission to the User Table (see {@link controller.AllUserController})
     */
    SHOW_ALL_USERS("Nutzer zeigen"),
    /**
     * Permission to the User Creation in the {@link controller.AllUserController}
     */
    CREATE_USER("Nutzer erstellen"),
    /**
     * Permission to the User Editing in the {@link controller.AllUserController}
     */
    EDIT_USER("Nutzer editieren"),
    /**
     * Permission to the User Deletion in the {@link controller.AllUserController}
     */
    DELETE_USER("Nutzer löschen"),
    /**
     * Permission to the Treatment Table (see {@link controller.AllTreatmentController})
     */
    SHOW_ALL_TREATMENTS("Behandlungen zeigen"),
    /**
     * Permission to the Treatment Creation (see {@link controller.NewTreatmentController})
     */
    CREATE_TREATMENT("Behandlung erstellen"),
    /**
     * Permission to the Treatment Editing (see {@link controller.TreatmentController})
     */
    EDIT_TREATMENT("Behandlung editieren"),
    /**
     * Permission to the Treatment Deletion in the {@link controller.AllTreatmentController}
     */
    DELETE_TREATMENT("Behandlung löschen"),
    /**
     * Permission to change the Password of a User Object (see {@link controller.ChangePasswordController})
     */
    CHANGE_PASSWORD("Passwort ändern");

    public final String description;

    PermissionKey(String description) {
        this.description = description;
    }
}
