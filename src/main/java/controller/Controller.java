package controller;

import datastorage.ConnectionBuilder;
import datastorage.UserSession;
import enums.PermissionKey;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.AlertCreator;

import java.io.IOException;
import java.util.Arrays;

/**
 * abstract class for the controller and permission managment
 *
 * please keep in mind, that the Controller is connected to the View programmatically {@link #create()},
 * adding your controller to your View-FXML will end in a total disaster.
 */
public abstract class Controller {
    protected Stage stage;

    /**
     * gets stage double-checks if user has permissions for the stage
     *
     * @return stage if user has permissions, else null
     */
    public Stage getStage() {
        if (stage == null) create();
        if (userIsPermitted()) {
            return stage;
        }
        createNoPermissionAlert();
        return null;
    }

    /**
     * method to create the stage and check for the users permissions
     *
     * @return stage, null if user is not permitted
     */
    public Stage create() {
        try {
            stage = new Stage();

            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource(getFxmlPath())
            );

            loader.setController(this);

            Parent root = loader.load();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(getWindowTitle() != null && getWindowTitle().length() > 0 ?
                    "NHPlus - " + getWindowTitle() : "NHPlus"
            );

            if (isClosingAppOnX()) {
                stage.setOnCloseRequest(event -> {
                    ConnectionBuilder.closeConnection();
                    Platform.exit();
                    System.exit(0);
                });
            }

            return stage;
        } catch (IOException ex) {
            ex.printStackTrace();
            AlertCreator.createError("Systemfehler", "Es ist beim Öffnen des gewünschten Fensters " +
                    "ein Fehler aufgetreten.");
            return null;
        }
    }

    /**
     * checks if user is permitted
     * @return boolean if user is permitted
     */
    public boolean userIsPermitted() {
        // if permitted groups is null, there are no permissions required for the controller
        if (getPermissionKey() == null) return true;
        return Arrays.asList(UserSession.getInstance().getGroup().getPermissions()).contains(getPermissionKey());
    }

    /**
     * creates alert when the user has no permissions
     */
    private void createNoPermissionAlert() {
        AlertCreator.createError("Keine Rechte", "Du hast keine Rechte um diese Aktion auszuführen")
                .show();
    }

    /**
     * default javafx initialize class
     */
    public abstract void initialize();

    /**
     * method to set window title to NHPlus - <code>TEXT</code>, if left empty, the window title will be set to NHPlus
     */
    public abstract String getWindowTitle();

    /**
     * overrideable method to define if the app should close on pressing x
     */
    public abstract boolean isClosingAppOnX();

    /**
     * overrideable method to define the fxml path
     */
    public abstract String getFxmlPath();

    /**
     * the permitted groups for the view and controller
     * return null if no permissions are required for a controller
     */
    public abstract PermissionKey getPermissionKey();
}
