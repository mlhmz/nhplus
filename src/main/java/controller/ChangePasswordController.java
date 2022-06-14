package controller;

import datastorage.DAOFactory;
import datastorage.UserDAO;
import enums.PermissionKey;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import model.User;
import utils.AlertCreator;
import utils.PasswordHashUtil;

import java.sql.SQLException;

/**
 * Logical Layer of the ChangePasswordView<br>
 * Handles the Process of changing and confirming the Password
 */
public class ChangePasswordController extends Controller {
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField newPasswordConfirmationField;

    private User user;

    private UserDAO userDAO;
    private boolean oldPasswordRequired;

    /**
     * custom initialize method to get the user into the controller
     */
    public void initialize(long uid, boolean oldPasswordRequired) {
        // Initial creation of User DAO
        userDAO = DAOFactory.getDAOFactory().createUserDAO();

        if (!oldPasswordRequired) {
            oldPasswordField.setDisable(true);
        }

        try {
            this.user = userDAO.read(uid);
        } catch (SQLException e) {
            createExceptionError();
            e.printStackTrace();
        }
    }

    private void createExceptionError() {
        AlertCreator.createError("Etwas ist schiefgelaufen...",
                "Beim Initialisieren des Passworts ist etwas schiefgelaufen. Bitte kontaktieren " +
                        "Sie den IT-Support oder versuchen Sie es später nochmal.").show();
    }

    /**
     * Event of the Password Confirm Button<br>
     * Validates the Fields, checks if the old Password is correct and changes the Password
     */
    @FXML
    public void onChangePasswordConfirm() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String newPasswordConfirmation = newPasswordConfirmationField.getText();

        if (!isEachFieldValid(oldPassword, newPassword, newPasswordConfirmation)) return;

        try {
            userDAO.updatePassword(user, newPassword);
        } catch (SQLException e) {
            createExceptionError();
            e.printStackTrace();
        }

        closeStage();
    }

    /**
     * Validates each Field in the View, combines the Validation Errors and warns the User with an alert box
     * created with the {@link AlertCreator}
     *
     *
     * @param oldPassword The old password, to make sure that it's not empty
     * @param newPassword The new password, to make sure it's not empty and 8 chars long
     * @param newPasswordConfirmation The new confirm Password, to make sure it's not empty and equals the new Password
     * @return boolean if the fields are valid
     */
    private boolean isEachFieldValid(String oldPassword, String newPassword, String newPasswordConfirmation) {
        boolean valid = true;
        StringBuilder sb = new StringBuilder();

        // Check if Fields are empty, these will be combined
        if (oldPasswordRequired && oldPassword.isEmpty()) {
            sb.append("Es wurde kein altes Passwort angegeben.\n");
            valid = false;
        }
        if (newPassword.isEmpty()) {
            sb.append("Es wurde kein neues Passwort angegeben.\n");
            valid = false;
        }
        if (newPasswordConfirmation.isEmpty()) {
            sb.append("Es wurde keine Bestätigung für das neue Passwort angegeben\n");
            valid = false;
        }

        // If some validation errors already happened here, it creates already a warning and returns
        // because the emptiness of fields is not as complex as the other things
        if (!valid) {
            AlertCreator.createWarning("Fehler bei der Erstellung", sb.toString()).show();
            return false;
        }

        // check if fields match the business logic
        if (oldPasswordRequired && !isOldPasswordCorrect(oldPassword)) {
            sb.append("Das alte Passwort ist nicht korrekt.\n");
            valid = false;
        }
        if (newPassword.length() < 8) {
            sb.append("Das Passwort ist zu kurz.\n");
            valid = false;
        }
        if (!newPasswordConfirmation.equals(newPassword)) {
            sb.append("Das bestätigte Passwort weicht vom neuen ab.\n");
            valid = false;
        }

        if (!valid) {
            AlertCreator.createWarning("Fehler bei der Erstellung", sb.toString()).show();
        }
        return valid;
    }

    /**
     * Checks if the given 'Old Password' matches to the users password hash
     *
     * @param oldPassword the password that was typed in to change the password
     * @return boolean if the typed in password is correct
     */
    private boolean isOldPasswordCorrect(String oldPassword) {
        return PasswordHashUtil.isValidPassword(oldPassword, user.getPassword());
    }

    /**
     * executes {@link #clearAndResetFields()} and closes stage
     */
    private void closeStage() {
        clearAndResetFields();
        stage.close();
    }

    /**
     * clears the fields in the view
     */
    private void clearAndResetFields() {
        oldPasswordField.setDisable(false);
        oldPasswordField.setText("");
        newPasswordField.setText("");
        newPasswordConfirmationField.setText("");
    }

    @Override
    public String getWindowTitle() {
        return "Passwort ändern";
    }

    @Override
    public boolean isClosingAppOnX() {
        return false;
    }

    @Override
    public String getFxmlPath() {
        return "/ChangePasswordView.fxml";
    }

    @Override
    public PermissionKey getPermissionKey() {
        return PermissionKey.CHANGE_PASSWORD;
    }
}
