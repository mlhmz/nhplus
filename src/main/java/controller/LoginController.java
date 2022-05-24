package controller;


import datastorage.ConnectionBuilder;
import datastorage.DAOFactory;
import datastorage.UserDAO;
import datastorage.UserSession;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.User;
import utils.AlertCreator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class LoginController {
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    Button btnLogin;

    private final UserDAO dao;

    private Stage stage;

    public LoginController() {
        dao = DAOFactory.getDAOFactory().createUserDAO();
    }

    public void initialize() throws IOException {
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

        if (!(users == null || users.isEmpty())) {
            return;
        }

        AlertCreator.createWarning("Warnung", "Anwendung nicht eingerichtet",
                "Es gibt keinen Nutzer, bitte richten Sie einen Administrator-Nutzer ein").showAndWait();

        createNewAccountController();


    }

    /**
     * sets the stage of the controller
     * mostly used by external classes
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void createNewAccountController() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/NewUserAccountView.fxml")
        );
        AnchorPane pane = loader.load();

        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setTitle("NHPlus - Nutzer erstellen");

        NewUserAccountController controller = loader.getController();
        controller.initialize(stage);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> stage.close());
    }

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

        if (!user.getPassword().equals(password)) {
            // executes alarm if password is wrong
            alert.show();
            return;
        }

        // creates user session with singleton
        UserSession userSession = UserSession.getInstance();
        userSession.init(user);

        openMainWindow();
    }



    private void openMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/MainWindowView.fxml")
            );
            BorderPane pane = loader.load();

            Scene scene = new Scene(pane);
            Stage mainWindowStage = new Stage();

            MainWindowController controller = loader.getController();
            controller.setStage(mainWindowStage);
            controller.setLoginStage(this.stage);

            mainWindowStage.setTitle("NHPlus");
            mainWindowStage.setScene(scene);
            mainWindowStage.setResizable(false);
            mainWindowStage.show();

            mainWindowStage.setOnCloseRequest(event -> {
                ConnectionBuilder.closeConnection();
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
