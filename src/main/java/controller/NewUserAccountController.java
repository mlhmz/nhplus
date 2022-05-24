package controller;

import datastorage.DAOFactory;
import datastorage.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import java.sql.SQLException;

public class NewUserAccountController {
    @FXML
    public TextField username;

    @FXML
    public PasswordField password;

    @FXML
    public TextField firstName;

    @FXML
    public TextField lastName;

    @FXML
    public Button btnCreate;

    @FXML
    public Button btnCancel;

    private final UserDAO dao;

    private Stage stage;

    public NewUserAccountController() {
        dao = DAOFactory.getDAOFactory().createUserDAO();
    }

    public void initialize(Stage stage) {
        this.stage = stage;
    }

    public void handleCreate() {
        // obtains data from gui fields
        String username = this.username.getText();
        String password = this.password.getText();
        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();

        User user = new User(username, password, firstName, lastName);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler beim Erstellen");

        try {
            dao.create(user);
        } catch (SQLException e) {
            alert.setHeaderText("Etwas ist schiefgelaufen...");
            alert.setContentText("Beim Erstellen des Nutzers ist ein Fehler aufgetreten");
            return;
        }

        stage.close();
    }

    public void handleCancel() {
        stage.close();
    }
}
