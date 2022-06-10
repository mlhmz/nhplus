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
        return groupListView -> new ListCell<Group>() {
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

    @FXML
    public void handleCreate(ActionEvent e) {
        // obtains data from gui fields
        String username = this.username.getText();
        String password = this.password.getText();
        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();
        Group group = this.groupComboBox.getValue();

        User user = new User(username, password, firstName, lastName, group);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler beim Erstellen");

        try {
            dao.create(user);
        } catch (SQLException ex) {
            alert.setHeaderText("Etwas ist schiefgelaufen...");
            alert.setContentText("Beim Erstellen des Nutzers ist ein Fehler aufgetreten");
            return;
        }

        clearFields();

        stage.close();
    }

    @FXML
    public void handleCancel(ActionEvent e) {
        stage.close();
    }

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
