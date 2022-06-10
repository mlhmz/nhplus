package controller;

import datastorage.UserSession;
import enums.PermissionKey;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 * homepage of the application
 */
public class MainWindowController extends Controller {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    public Text userSessionText;

    private Controller allPatientController, allTreatmentController;

    public void initialize() {
        setUserSessionLabel();
    }

    private void setUserSessionLabel() {
        UserSession userSession = UserSession.getInstance();
        String userLabel =
                userSession.getUsername() + " - " + userSession.getFirstName() + " " + userSession.getLastName();

        userSessionText.setText(userLabel);
    }

    @FXML
    private void handleShowAllPatient(ActionEvent e) {
        if (allPatientController == null) {
            allPatientController = new AllPatientController();
        }
        mainBorderPane.setCenter(allPatientController.getStage().getScene().getRoot());
    }

    @FXML
    private void handleShowAllTreatments(ActionEvent e) {
        if (allTreatmentController == null) {
            allTreatmentController = new AllTreatmentController();
        }
        mainBorderPane.setCenter(allTreatmentController.getStage().getScene().getRoot());
    }

    @FXML
    private void handleLogout() {
        mainBorderPane.setCenter(null);
        UserSession.getInstance().clear();
        ControllerManager.getInstance().getLoginStage().show();
        stage.close();
    }

    @Override
    public String getWindowTitle() {
        // Window Title is left empty here because the main window should only show "NHPlus"
        return null;
    }

    @Override
    public boolean isClosingAppOnX() {
        return true;
    }

    @Override
    public String getFxmlPath() {
        return "/MainWindowView.fxml";
    }

    @Override
    public PermissionKey getPermissionKey() {
        return PermissionKey.SHOW_HOMEPAGE;
    }
}
