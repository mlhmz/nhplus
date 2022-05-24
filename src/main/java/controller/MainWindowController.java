package controller;

import datastorage.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class MainWindowController {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    public Text userSessionText;

    private Stage stage;
    private Stage loginStage;

    public void initialize() {
        setUserSessionLabel();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLoginStage(Stage stage) {
        this.loginStage = stage;
    }

    private void setUserSessionLabel() {
        UserSession userSession = UserSession.getInstance();
        String userLabel =
                userSession.getUsername() + " - " + userSession.getFirstName() + " " + userSession.getLastName();

        userSessionText.setText(userLabel);
    }

    @FXML
    private void handleShowAllPatient(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/AllPatientView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AllPatientController controller = loader.getController();
    }

    @FXML
    private void handleShowAllTreatments(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/AllTreatmentView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AllTreatmentController controller = loader.getController();
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().clear();
        loginStage.show();
        stage.close();
    }
}
