package controller;

import datastorage.PatientDAO;
import enums.PermissionKey;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Patient;
import utils.AlertCreator;
import utils.DateUtils;
import datastorage.DAOFactory;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


/**
 * The <code>AllPatientController</code> contains the entire logic of the patient view. It determines which data is displayed and how to react to events.
 */
public class AllPatientController extends Controller {
    @FXML
    private TableView<Patient> tableView;
    @FXML
    private TableColumn<Patient, Integer> colID;
    @FXML
    private TableColumn<Patient, String> colFirstName;
    @FXML
    private TableColumn<Patient, String> colSurname;
    @FXML
    private TableColumn<Patient, String> colDateOfBirth;
    @FXML
    private TableColumn<Patient, String> colCareLevel;
    @FXML
    private TableColumn<Patient, String> colRoom;
    @FXML
    private TableColumn<Patient, Date> colLockDate;

    @FXML
    Button btnDelete;
    @FXML
    Button btnAdd;
    @FXML
    TextField txtSurname;
    @FXML
    TextField txtFirstname;
    @FXML
    TextField txtBirthday;
    @FXML
    TextField txtCarelevel;
    @FXML
    TextField txtRoom;

    private ObservableList<Patient> tableviewContent = FXCollections.observableArrayList();
    private PatientDAO dao;

    /**
     * Initializes the corresponding fields. Is called as soon as the corresponding FXML file is to be displayed.
     */
    public void initialize() {
        readAllAndShowInTableView();

        this.colID.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("pid"));

        //CellValuefactory zum Anzeigen der Daten in der TableView
        this.colFirstName.setCellValueFactory(new PropertyValueFactory<Patient, String>("firstName"));
        //CellFactory zum Schreiben innerhalb der Tabelle
        this.colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colSurname.setCellValueFactory(new PropertyValueFactory<Patient, String>("surname"));
        this.colSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colDateOfBirth.setCellValueFactory(new PropertyValueFactory<Patient, String>("dateOfBirth"));
        this.colDateOfBirth.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colCareLevel.setCellValueFactory(new PropertyValueFactory<Patient, String>("careLevel"));
        this.colCareLevel.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colRoom.setCellValueFactory(new PropertyValueFactory<Patient, String>("roomnumber"));
        this.colRoom.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colLockDate.setCellValueFactory(new PropertyValueFactory<Patient, Date>("lockDate"));

        // Converts Date to an actual indicator when and if even the patient is getting locked
        Callback<TableColumn<Patient, Date>, TableCell<Patient, Date>> lockDateTableFactory =
                tableColumn -> new LockMarkDateFormatCell();

        // Sets the CellFactory with the Date Formatter
        this.colLockDate.setCellFactory(lockDateTableFactory);

        //Anzeigen der Daten
        this.tableView.setItems(this.tableviewContent);

        if (!isPermittedUserToSpecificOperation(PermissionKey.EDIT_PATIENT)) {
            this.tableView.setEditable(false);
        }
    }

    /**
     * method to create the stage and check for the users permissions
     * also checks if user has permission for editing and sets the editable state to the result of the check
     *
     * @return stage, null if user is not permitted
     */
    @Override
    public Stage getStage() {
        Stage stage = super.getStage();
        tableView.setEditable(isPermittedUserToSpecificOperation(PermissionKey.EDIT_CAREGIVER));
        return stage;
    }

    /**
     * handles new firstname value
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Patient, String> event){
        event.getRowValue().setFirstName(event.getNewValue());
        doUpdate(event);
    }

    /**
     * handles new surname value
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Patient, String> event){
        event.getRowValue().setSurname(event.getNewValue());
        doUpdate(event);
    }

    /**
     * handles new birthdate value
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditDateOfBirth(TableColumn.CellEditEvent<Patient, String> event){
        event.getRowValue().setDateOfBirth(event.getNewValue());
        doUpdate(event);
    }

    /**
     * handles new carelevel value
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditCareLevel(TableColumn.CellEditEvent<Patient, String> event){
        event.getRowValue().setCareLevel(event.getNewValue());
        doUpdate(event);
    }

    /**
     * handles new roomnumber value
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditRoomnumber(TableColumn.CellEditEvent<Patient, String> event){
        event.getRowValue().setRoomnumber(event.getNewValue());
        doUpdate(event);
    }

    /**
     * updates a patient by calling the update-Method in the {@link PatientDAO}
     * @param t row to be updated by the user (includes the patient)
     */
    private void doUpdate(TableColumn.CellEditEvent<Patient, String> t) {
        try {
            dao.update(t.getRowValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * calls readAll in {@link PatientDAO} and shows patients in the table
     */
    private void readAllAndShowInTableView() {
        this.tableviewContent.clear();
        this.dao = DAOFactory.getDAOFactory().createPatientDAO();
        List<Patient> allPatients;
        try {
            allPatients = dao.readAll();
            for (Patient p : allPatients) {
                this.tableviewContent.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles a Lock Event, marks the Patient with a Date when he gets locked
     */
    @FXML
    public void handleToggleLockMarkSelectedPatient() {
        // checks if user has permission
        if (!isPermittedUserToSpecificOperation(PermissionKey.LOCK_AND_UNLOCK_PATIENT)) {
            createNoPermissionAlert(PermissionKey.LOCK_AND_UNLOCK_PATIENT);
            return;
        }

        // gets patients uid from table and gets it from db in order to get the latest
        // update of the user
        long pid = this.tableView.getSelectionModel().getSelectedItem().getPid();
        Patient selectedPatient = null;
        try {
            selectedPatient = dao.read(pid);
            if (selectedPatient == null) {
                throw new Exception("Der Nutzer aus der Tabelle konnte nicht gefunden werden.");
            }
        } catch (Exception e) {
            AlertCreator.createError("Etwas ist schiefgelaufen...",
                    "Bei der Erkennung ob der Nutzer aktuell gesperrt oder entsperrt ist," +
                            "gab es einen Fehler.").show();
            e.printStackTrace();
            return;
        }

        // building message
        StringBuilder sb = new StringBuilder();
        sb.append(selectedPatient.getFirstName()).append(" ").append(selectedPatient.getSurname())
                .append(" wurde ");

        // if lock date field is not empty, delete lockMark / lockDate
        if (selectedPatient.getLockDate() != null) {
            try {
                dao.removeLockMarkByPid(selectedPatient.getPid());
            } catch (SQLException e) {
                AlertCreator.createError("Etwas ist schiefgelaufen...",
                        "Es gab einen Fehler in der Datenbank beim Entsperren des Nutzers").show();
                e.printStackTrace();
                return;
            }
            sb.append("die Markierung zur Sperre aufgehoben.");
        } else {
            // creates new date and appends 24 hours to it
            Date lockDate = DateUtils.appendHoursToDate(new Date(), 24);
            try {
                dao.markLockByPid(selectedPatient.getPid(), lockDate);
            } catch (SQLException e) {
                AlertCreator.createError("Etwas ist schiefgelaufen...",
                        "Es gab einen Fehler in der Datenbank beim Sperren des Nutzers").show();
                e.printStackTrace();
                return;
            }
            sb.append("zum Sperren markiert, die Sperrung erfolgt in 24 Stunden.");
        }
        AlertCreator.createInformation("Patienteninformation", sb.toString()).show();
        readAllAndShowInTableView();
    }

    /**
     * handles a add-click-event. Creates a patient and calls the create method in the {@link PatientDAO}
     */
    @FXML
    public void handleAdd() {
        if (!isPermittedUserToSpecificOperation(PermissionKey.CREATE_PATIENT)) {
            createNoPermissionAlert(PermissionKey.CREATE_PATIENT);
            return;
        }

        String surname = this.txtSurname.getText();
        String firstname = this.txtFirstname.getText();
        String birthday = this.txtBirthday.getText();
        LocalDate date = DateUtils.convertStringToLocalDate(birthday);
        String carelevel = this.txtCarelevel.getText();
        String room = this.txtRoom.getText();
        try {
            Patient p = new Patient(firstname, surname, date, carelevel, room, null);
            dao.create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        readAllAndShowInTableView();
        clearTextfields();
    }

    /**
     * removes content from all textfields
     */
    private void clearTextfields() {
        this.txtFirstname.clear();
        this.txtSurname.clear();
        this.txtBirthday.clear();
        this.txtCarelevel.clear();
        this.txtRoom.clear();
    }

    @Override
    public String getWindowTitle() {
        return "Patienten";
    }

    @Override
    public boolean isClosingAppOnX() {
        return false;
    }

    @Override
    public String getFxmlPath() {
        return "/AllPatientView.fxml";
    }

    @Override
    public PermissionKey getPermissionKey() {
        return PermissionKey.SHOW_ALL_PATIENTS;
    }

    /**
     * Formats <code>lockDate</code> Cell to the <code>lockDate</code> in Red or, if the <code>lockDate</code> is null,
     * it clears the <code>lockDate</code>
     */
    private class LockMarkDateFormatCell extends TableCell<Patient, Date> {
        public LockMarkDateFormatCell() {}

        @Override
        protected void updateItem(Date date, boolean b) {
            super.updateItem(date, b);

            // When no lockMarkDate is given, the Content of the TableCell will be /
            if (date == null) {
                setText("");
                setTextFill(Color.BLACK);
                return;
            }

            // if however, the patient is about to get locked, we'll show when he's getting locked
            String pattern = "dd.MM.yyyy HH:mm:ss";
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            setText(formatter.format(date));
            setTextFill(Color.RED);

        }
    }
}