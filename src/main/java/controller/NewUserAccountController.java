package controller;

import datastorage.DAOFactory;
import datastorage.UserDAO;
import enums.PermissionKey;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.Group;
import model.GroupFactory;
import model.User;
import utils.AlertCreator;

import java.sql.SQLException;

public class NewUserAccountController extends Controller {
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

    @FXML
    public ComboBox<Group> groupComboBox;

    private final UserDAO dao;

    public NewUserAccountController() {
        dao = DAOFactory.getDAOFactory().createUserDAO();
    }

    /**
     * initialize-method that is executed automatically by the controller
     * Fills Groups in the Combobox
     */
    @Override
    public void initialize() {
        Callback<ListView<Group>, ListCell<Group>> cellFactory = buildCallback();

        groupComboBox.setButtonCell(cellFactory.call(null));
        groupComboBox.setCellFactory(cellFactory);

        groupComboBox.setItems(FXCollections.observableArrayList(GroupFactory.getInstance().getAllGroups()));
    }

    /**
     * the combobox needs a callback in order to show the group objects right
     */
    private Callback<ListView<Group>, ListCell<Group>> buildCallback() {
        return groupListView -> new ListCell<>() {
                    @Override
                    protected void updateItem(Group group, boolean empty) {
                        super.updateItem(group, empty);
                        if (group == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(group.getGuiRepresentation());
                        }
                    }
                };
    }

    /**
     * creates a user if all fields are filled
     * triggered by Create Button
     */
    @FXML
    public void handleCreate(ActionEvent e) {
        // obtains data from gui fields
        String username = this.username.getText();
        String password = this.password.getText();
        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();
        Group group = this.groupComboBox.getValue();

        try {
            validateFields(username, password, firstName, lastName, group);
        } catch (IllegalArgumentException ex) {
            AlertCreator.createWarning("Fehler bei der Erstellung", ex.getMessage());
        }

        User user = new User(username, password, firstName, lastName, group);


        try {
            dao.create(user);
        } catch (SQLException ex) {
            AlertCreator.createError("Etwas ist schiefgelufen...",
                    "Beim Erstellen des Nutzers ist ein Fehler aufgetreten");
            return;
        }

        clearFields();

        stage.close();
    }

    /**
     * Checks if Fields are valid
     *
     * @throws IllegalArgumentException Exception with the Validation Error as Message
     */
    private void validateFields(String username, String password, String firstName, String lastName, Group group)
            throws IllegalArgumentException {
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Der Nutzername ist leer.");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("Das Passwort ist leer.");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Das Passwort muss länger als 8 Zeichen sein.");
        }
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("Der Vorname ist leer.");
        }
        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("Der Nachname ist leer.");
        }
        if (group == null) {
            // usually the group cant be empty, this is a fallback option
            throw new IllegalArgumentException("Die Gruppe ist leer.");
        }
    }

    /**
     * closes stage, triggered by cancel button
     */
    @FXML
    public void handleCancel(ActionEvent e) {
        clearFields();
        stage.close();
    }

    /**
     * clears fields
     */
    private void clearFields() {
        this.username.setText("");
        this.password.setText("");
        this.firstName.setText("");
        this.lastName.setText("");
    }

    @Override
    public String getWindowTitle() {
        return "Nutzer erstellen";
    }

    @Override
    public boolean isClosingAppOnX() {
        return false;
    }

    @Override
    public String getFxmlPath() {
        return "/NewUserAccountView.fxml";
    }

    @Override
    public PermissionKey getPermissionKey() {
        return PermissionKey.CREATE_USER;
    }
}
