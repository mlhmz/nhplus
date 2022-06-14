package controller;


import datastorage.ConnectionBuilder;
import datastorage.DAOFactory;
import datastorage.UserDAO;
import datastorage.UserSession;
import enums.PermissionKey;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.GroupFactory;
import model.User;
import utils.AlertCreator;
import utils.PasswordHashUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * logic of the login process
 */
public class LoginController extends Controller {
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    Button btnLogin;


    private final UserDAO dao;


    public LoginController() {
        dao = DAOFactory.getDAOFactory().createUserDAO();
    }

    @Override
    public void initialize() {
        List<User> users = null;

        try {
            users = dao.readAll();
        } catch (SQLException ex) {
            // Verlässt bei fehlerhaften / nicht eingerichteter Datenbank die Anwendung
            AlertCreator.createError("Anwendungsfehler", "Ein interner Fehler ist aufgetreten",
                    " die Datenbank wurde nicht eingerichtet.").showAndWait();
            ConnectionBuilder.closeConnection();
            System.exit(0);
        }

        if (!(users == null || users.isEmpty())) return;

        AlertCreator.createWarning("Warnung", "Anwendung nicht eingerichtet",
                "Es gibt keinen Nutzer, bitte richten Sie einen Administrator-Nutzer ein").showAndWait();

        createInitialUserCreationDialog();
    }

    /**
     * handles the login button and validates the password hash
     */
    @FXML
    public void handleLogin() {
        // obtains data from gui fields
        String username = this.username.getText();
        String password = this.password.getText();

        // creates alert if data is wrong
        Alert alert = AlertCreator.createError("Fehlerhafte Operation", "Fehler beim Login",
                "Ihre Logindaten sind ungültig.");

        User user;

        try {
            // gets user object from dao
            user = dao.readByUsername(username);
        } catch (SQLException exception) {
            // executes alarm if username is wrong
            alert.show();
            return;
        }

        if (user == null) {
            alert.show();
            return;
        }

        boolean validPassword = PasswordHashUtil.isValidPassword(password, user.getPassword());
        if (!validPassword) {
            // executes alarm if password is wrong
            alert.show();
            return;
        }

        // creates user session with singleton
        UserSession userSession = UserSession.getInstance();
        userSession.init(user);

        clearFields();

        ControllerManager.getInstance().getMainStage().show();
        stage.close();
    }

    /**
     * clears the fields after the login is done
     */
    private void clearFields() {
        this.username.setText("");
        this.password.setText("");
    }

    /**
     * creates little dialog for the creation of an admin user
     */
    private void createInitialUserCreationDialog() {
            Dialog<User> dialog = new Dialog<>();
            dialog.setTitle("NHPlus - Einrichtung");
            dialog.setHeaderText("Admin-Nutzer Erstellung");
            dialog.setHeight(400);
            dialog.setWidth(200);
            dialog.setResizable(false);

            Label userNameLabel = new Label("Nutzername: ");
            TextField userNameField = new TextField();

            Label passwordLabel = new Label("Passwort: ");
            TextField passwordField = new PasswordField();

            Label firstNameLabel = new Label("Vorname: ");
            TextField firstNameField = new TextField();

            Label lastNameLabel = new Label("Nachname: ");
            TextField lastNameField = new TextField();

            GridPane grid = new GridPane();

            grid.add(userNameLabel, 1, 1);
            grid.add(userNameField, 2, 1);

            grid.add(passwordLabel, 1, 2);
            grid.add(passwordField, 2, 2);

            grid.add(firstNameLabel, 1, 3);
            grid.add(firstNameField, 2, 3);

            grid.add(lastNameLabel, 1, 4);
            grid.add(lastNameField, 2, 4);

            grid.setVgap(5);

            dialog.getDialogPane().setContent(grid);

            ButtonType createButton = new ButtonType("Erstellen", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().add(createButton);
            dialog.getDialogPane().getButtonTypes().add(cancelButton);

            dialog.setResultConverter(buttonType -> {
                switch (buttonType.getButtonData()) {
                    case OK_DONE:
                        return new User(
                                userNameField.getText(),
                                passwordField.getText(),
                                firstNameField.getText(),
                                lastNameField.getText(),
                                GroupFactory.getInstance().getGroup("ADMIN")
                        );
                    case CANCEL_CLOSE:
                        // not using a break to use default as fallback
                        ConnectionBuilder.closeConnection();
                        Platform.exit();
                        System.exit(0);
                    default:
                        // if any other button type is pressed, should not happen because there are only
                        // two buttons
                        return null;
                }
            });

            Optional<User> user = dialog.showAndWait();

            if (user.isPresent()) {
                dialog.close();
                try {
                    dao.create(user.get());
                } catch (SQLException e) {
                    AlertCreator.createError("Anwendungsfehler", "Es ist ein Fehler beim Erstellen" +
                            "des Nutzers aufgetreten. Bitte überprüfen Sie die Datenbank. Die Anwendung wird jetzt " +
                            "geschlossen.");
                    ConnectionBuilder.closeConnection();
                    Platform.exit();
                    System.exit(0);
                }
            }
    }

    @Override
    public String getWindowTitle() {
        return "Login";
    }

    @Override
    public boolean isClosingAppOnX() {
        return true;
    }

    @Override
    public String getFxmlPath() {
        return "/LoginView.fxml";
    }

    /**
     * returns null, because this class doesn't need any permissions
     */
    @Override
    public PermissionKey getPermissionKey() {
        return null;
    }
}
