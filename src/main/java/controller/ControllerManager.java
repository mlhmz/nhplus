package controller;

import javafx.stage.Stage;

/**
 * Singleton to keep the Controller-Instances in the Memory
 */
public class ControllerManager {
    private static ControllerManager instance;
    private Controller mainController;
    private Controller loginController;

    /**
     * static getInstance()-Method to get the Singleton Object
     * for a thread-safe behaviour, the method is synchronized
     */
    public synchronized static ControllerManager getInstance() {
        if (instance == null) {
            instance = new ControllerManager();
        }
        return instance;
    }

    /**
     * Singleton-Like Getter to get the main-Controller
     */
    public Controller getMainController() {
        if (mainController == null) {
            mainController = new MainWindowController();
        }
        return mainController;
    }

    /**
     * Getter for the mainStage, fires getMainController() in order to make sure there is an instance
     * for the MainController
     */
    public Stage getMainStage() {
        // using the getter of the mainController to make sure, the mainController has an instance
        return getMainController().getStage();
    }

    /**
     * Singleton-Like Getter to get the Login-Controller
     */
    public Controller getLoginController() {
        if (loginController == null) {
            loginController = new LoginController();
        }
        return loginController;
    }

    /**
     * Getter for the loginStage, has the same behaviour as {@link #getMainStage()} for the loginController
     */
    public Stage getLoginStage() {
        // using the getter of the loginController to make sure, that the loginController has an instance
        return getLoginController().getStage();
    }
}
