package controller;

import datastorage.UserSession;
import enums.Group;
import javafx.stage.Stage;
import utils.AlertCreator;

import java.io.IOException;
import java.util.Arrays;

/**
 * abstract class for the controller and permission managment
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
            stage = loadViewAndCreateStage();
            stage.setTitle(getWindowTitle() != null && getWindowTitle().length() > 0 ?
                    "NHPlus - " + getWindowTitle() : "NHPlus"
            );
            return stage;
        } catch (IOException ex) {
            ex.printStackTrace();
            AlertCreator.createError("Systemfehler", "Es ist beim Öffnen des gewünschten Fensters " +
                    "ein Fehler aufgetreten.");
            return null;
        }
    }

    /**
     * opens controller, and initializes it if it's not already initialized
     *
     * @param controller the controller to open
     * @return the stage of the controller
     */
    protected Stage openController(Controller controller) {
        Stage stage;

        // checks if controller already exists, if so get it, else create it
        if (controller.getStage() == null) {
            stage = controller.create();
        } else {
            stage = controller.getStage();
        }

        // returns if stage == null which happens when user has no permissions
        // just a fallback option in this case because only admins can use the AllUserController
        if (stage == null) return null;

        // show the stage and return it
        stage.show();
        return stage;
    }

    /**
     * checks if user is permitted
     * @return boolean if user is permitted
     */
    public boolean userIsPermitted() {
        // if permitted groups is null, there are no permissions required for the controller
        if (getPermittedGroups() == null) return true;
        return Arrays.stream(getPermittedGroups()).anyMatch(group -> group.equals(UserSession.getInstance().getGroup()));
    }

    /**
     * creates alert when the user has no permissions
     */
    private void createNoPermissionAlert() {
        AlertCreator.createError("Keine Rechte", "Du hast keine Rechte um diese Aktion auszuführen");
    }

    /**
     * abstract method for the logic of the view loading and stage creation
     */
    protected abstract Stage loadViewAndCreateStage() throws IOException;

    /**
     * default javafx initialize class
     */
    public abstract void initialize();

    /**
     * method to set window title to NHPlus - <code>TEXT</code>, if left empty, the window title will be set to NHPlus
     */
    public abstract String getWindowTitle();

    /**
     * the permitted groups for the view and controller
     * return null if no permissions are required for a controller
     */
    public abstract Group[] getPermittedGroups();
}
