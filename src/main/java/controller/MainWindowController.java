package controller;

import datastorage.UserSession;
import enums.PermissionKey;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * homepage of the application
 */
public class MainWindowController extends Controller {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    public Text userSessionText;
    @FXML
    public Button treatmentsBtn, patientsBtn, usersBtn, logoutBtn;
    @FXML
    public VBox vBox;

    private Controller allPatientController, allTreatmentController, allUsersController;

    /**
     * intializes the stage
     */
    public void initialize() {
        setUserSessionLabel();

        allPatientController = new AllPatientController();
        allTreatmentController = new AllTreatmentController();
        allUsersController = new AllUserController();
    }

    /**
     * gets the stage and adjusts the button to the permissions of the user
     */
    @Override
    public Stage getStage() {
        Stage stage = super.getStage();

        // removes buttons for modules that the user has no permissions for
        if (!allPatientController.isPermittedUser()) {
            vBox.getChildren().remove(patientsBtn);
        }
        if (!allTreatmentController.isPermittedUser()) {
            vBox.getChildren().remove(treatmentsBtn);
        }
        if (!allUsersController.isPermittedUser()) {
            vBox.getChildren().remove(usersBtn);
        }

        return stage;
    }

    /**
     * sets the user session label at the bottom of the screen
     */
    private void setUserSessionLabel() {
        UserSession userSession = UserSession.getInstance();
        String userLabel =
                userSession.getUsername() + " - " + userSession.getFirstName() + " " + userSession.getSurname();

        userSessionText.setText(userLabel);
    }

    /**
     * shows all patients by pressing the Patient Button
     */
    @FXML
    private void handleShowAllPatient(ActionEvent e) {
        mainBorderPane.setCenter(allPatientController.getStage().getScene().getRoot());
    }

    /**
     * shows all treatments by pressing the Treatment Button
     */
    @FXML
    private void handleShowAllTreatments(ActionEvent e) {
        mainBorderPane.setCenter(allTreatmentController.getStage().getScene().getRoot());
    }

    /**
     * shows all users by pressing the Users Button
     */
    @FXML
    private void handleShowAllUsers(ActionEvent e) {
        mainBorderPane.setCenter(allUsersController.getStage().getScene().getRoot());
    }

    @FXML
    private void handleChangePassword() {
        ChangePasswordController controller = new ChangePasswordController();
        Stage stage = controller.getStage();
        controller.initialize(UserSession.getInstance().getUid(), true);
        stage.show();
    }

    /**
     * logs the user out, resets the scene, deletes the session, shows the login stage, closes the MainWindowStage
     * triggered by the logout button
     */
    @FXML
    private void handleLogout() {
        resetScene();
        // actual logout
        UserSession.getInstance().clear();
        ControllerManager.getInstance().getLoginStage().show();
        stage.close();
    }

    /**
     * resets scene to enter state
     */
    private void resetScene() {
        // resets all buttons in vbox
        if (!vBox.getChildren().contains(patientsBtn)) {
            vBox.getChildren().add(patientsBtn);
        }
        if (!vBox.getChildren().contains(treatmentsBtn)) {
            vBox.getChildren().add(treatmentsBtn);
        }
        if (!vBox.getChildren().contains(usersBtn)) {
            vBox.getChildren().add(usersBtn);
        }
        // moves logoutBtn to the end of the children list
        if (!vBox.getChildren().get(vBox.getChildren().size() - 1).equals(logoutBtn)) {
            vBox.getChildren().remove(logoutBtn);
            vBox.getChildren().add(logoutBtn);
        }

        // clears center
        mainBorderPane.setCenter(null);
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
