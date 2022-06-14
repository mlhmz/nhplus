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
 * abstract class for the controller and permission managment<br>
 * <br>
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
        if (isPermittedUser()) {
            return stage;
        }
        createNoPermissionAlert(getPermissionKey());
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
     *
     * @return boolean if user is permitted
     */
    public boolean isPermittedUser() {
        // if permitted groups is null, there are no permissions required for the controller
        if (getPermissionKey() == null) return true;
        return isPermittedUserToSpecificOperation(getPermissionKey());
    }

    /**
     * checks if user has given permission
     *
     * @param key specific permission
     * @return boolean if user has specific permission
     */
    public boolean isPermittedUserToSpecificOperation(PermissionKey key) {
        return Arrays.asList(UserSession.getInstance().getGroup().getPermissions()).contains(key);
    }

    /**
     * creates alert when the user has no permissions
     */
    protected void createNoPermissionAlert() {
        createNoPermissionAlert(null);
    }

    /**
     * creates alert when the user has no permissions
     */
    protected void createNoPermissionAlert(PermissionKey permissionKey) {
        String contentText;
        if (permissionKey == null) {
            contentText = "Sie haben keine Rechte um diese Operation auszuführen";
        } else {
            contentText = String.format("Sie haben keine Rechte um die Operation '%s' auszuführen.",
                    permissionKey);
        }
        AlertCreator.createError("Keine Rechte", contentText)
                .show();
    }

    /**
     * default javafx initialize class
     */
    public void initialize() {

    }

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
