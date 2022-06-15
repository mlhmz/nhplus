package controller;

import datastorage.CaregiverDAO;
import datastorage.DAOFactory;
import datastorage.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import enums.PermissionKey;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Patient;
import model.Treatment;
import model.Caregiver;
import utils.DateUtils;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class NewTreatmentController extends Controller {
    @FXML
    private Label lblSurname;
    @FXML
    private Label lblFirstname;
    @FXML
    private TextField txtBegin;
    @FXML
    private TextField txtEnd;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextArea taRemarks;
    @FXML
    private DatePicker datepicker;
    @FXML
    private ComboBox<Caregiver> caregiverComboBox;

    private AllTreatmentController controller;
    private Patient patient;
    private CaregiverDAO caregiverDAO;

    public void initialize(AllTreatmentController controller, Patient patient) {
        this.controller = controller;
        this.patient = patient;

        caregiverDAO = DAOFactory.getDAOFactory().createCaregiverDAO();

        showPatientData();

        Callback<ListView<Caregiver>, ListCell<Caregiver>> cellFactory = buildCallback();

        caregiverComboBox.setButtonCell(cellFactory.call(null));
        caregiverComboBox.setCellFactory(cellFactory);

        try {
            caregiverComboBox.setItems(FXCollections.observableList(caregiverDAO.readAll()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showPatientData(){
        this.lblFirstname.setText(patient.getFirstName());
        this.lblSurname.setText(patient.getSurname());
    }

    /**
     * the combobox needs a callback in order to show the caregiver objects right
     */
    private Callback<ListView<Caregiver>, ListCell<Caregiver>> buildCallback() {
        return groupListView -> new ListCell<>() {
            @Override
            protected void updateItem(Caregiver caregiver, boolean empty) {
                super.updateItem(caregiver, empty);
                if (caregiver == null || empty) {
                    setGraphic(null);
                } else {
                    setText(caregiver.getFirstName() + " " + caregiver.getSurname());
                }
            }
        };
    }

    @FXML
    public void handleAdd(){
        LocalDate date = this.datepicker.getValue();
        String s_begin = txtBegin.getText();
        LocalTime begin = DateUtils.convertStringToLocalTime(txtBegin.getText());
        LocalTime end = DateUtils.convertStringToLocalTime(txtEnd.getText());
        String description = txtDescription.getText();
        String remarks = taRemarks.getText();
        Treatment treatment = new Treatment(patient.getPid(), date,
                begin, end, description, remarks, caregiverComboBox.getValue().getCid());
        createTreatment(treatment);
        controller.readAllAndShowInTableView();
        stage.close();
    }

    private void createTreatment(Treatment treatment) {
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        try {
            dao.create(treatment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel(){
        stage.close();
    }

    @Override
    public void initialize() {

    }

    @Override
    public String getWindowTitle() {
        return "Behandlung erstellen";
    }

    @Override
    public boolean isClosingAppOnX() {
        return false;
    }

    @Override
    public String getFxmlPath() {
        return "/NewTreatmentView.fxml";
    }

    @Override
    public PermissionKey getPermissionKey() {
        return PermissionKey.CREATE_TREATMENT;
    }
}