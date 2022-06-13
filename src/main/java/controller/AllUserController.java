package controller;

import datastorage.DAOFactory;
import datastorage.UserDAO;
import enums.PermissionKey;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import model.Group;
import model.GroupFactory;
import model.User;
import utils.AlertCreator;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Contains logic of the User-View
 */
public class AllUserController extends Controller {
    @FXML
    public TableView<User> tableView;
    @FXML
    public TableColumn<User, Long> colID;
    @FXML
    public TableColumn<User, String> colLastName;
    @FXML
    public TableColumn<User, String> colFirstName;
    @FXML
    public TableColumn<User, String> colUsername;
    @FXML
    public TableColumn<User, Group> colGroup;
    @FXML
    public Button btnAdd;
    @FXML
    public Button btnDelete;

    private ObservableList<User> tableViewContent = FXCollections.observableArrayList();

    private UserDAO dao;

    private final NewUserAccountController newUserAccountController = new NewUserAccountController();

    /**
     * initializes the view, mainly the table
     */
    @Override
    public void initialize() {
        readAllDataAndShowInTableView();
        ObservableList<Group> cbValues = FXCollections.observableArrayList(GroupFactory.getInstance().getAllGroups());

        this.colID.setCellValueFactory(new PropertyValueFactory<>("uid"));

        this.colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        this.colLastName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        // the username is, like the uid, unique and not editable!!
        this.colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        // creates an editable cell combobox
        this.colGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
        colGroup.setCellFactory(ComboBoxTableCell.forTableColumn(new GroupStringConverter(), cbValues));

        tableView.setItems(tableViewContent);
    }

    /**
     * gets data from database and fills it into table
     */
    public void readAllDataAndShowInTableView() {
        this.tableViewContent.clear();
        if (dao == null) {
            dao = DAOFactory.getDAOFactory().createUserDAO();
        }
        List<User> allUsers;
        try {
            allUsers = dao.readAll();
            this.tableViewContent.addAll(allUsers);
        } catch (SQLException ex) {
            ex.printStackTrace();
            createDatabaseErrorAlert();
        }

    }

    /**
     * Opens a Window with the NewUserAccountController-View
     */
    @FXML
    public void handleCreate() {
        newUserAccountController.getStage().show();
    }

    /**
     * Deletes the Selected Entry in the Table
     */
    @FXML
    public void handleDelete() {
        if (this.tableView.getSelectionModel().getSelectedItem() == null) {
            AlertCreator.createWarning("Es ist kein Nutzer ausgewählt!", "Bitte wählen Sie einen " +
                    "Nutzer aus.");
            return;
        }
        User selectedUser = this.tableView.getSelectionModel().getSelectedItem();
        // because we're directly adding on the controller call the data, we don't have to instantiate the dao again
        try {
            dao.deleteById(selectedUser.getUid());
        } catch (SQLException ex) {
            ex.printStackTrace();
            createDatabaseErrorAlert();
        }
        refreshTable();
    }

    /**
     * Creates with the {@link AlertCreator} an Alert to inform the user that something with the Database went wrong
     */
    private void createDatabaseErrorAlert() {
        AlertCreator.createError("Anwendungsfehler", "Irgendetwas ist beim Schreiben in die" +
                "Datenbank schief gelaufen... Bitte versuchen Sie es erneut oder kontaktieren Sie den IT-Support");
    }

    /**
     * Refreshes Table on Button Press
     */
    @FXML
    public void handleRefresh() {
        refreshTable();
    }


    /**
     * handler to edit first name field
     */
    @FXML
    public void handleOnEditFirstName(TableColumn.CellEditEvent<User, String> event) {
        event.getRowValue().setFirstName(event.getNewValue());
        doUpdate(event);
        refreshTable();
    }

    /**
     * handler to edit last name field
     */
    @FXML
    public void handleOnEditLastName(TableColumn.CellEditEvent<User, String> event) {
        event.getRowValue().setLastName(event.getNewValue());
        doUpdate(event);
        refreshTable();
    }

    /**
     * handler to edit group field
     */
    @FXML
    public void handleOnEditGroup(TableColumn.CellEditEvent<User, Group> event) {
        event.getRowValue().setGroup(event.getNewValue());
        doUpdate(event);
        refreshTable();
    }

    /**
     * Updates TableRow / User-Object in Database
     *
     * @param event Event containing old and new Data
     * @param <T> object that got changed
     */
    private <T> void doUpdate(TableColumn.CellEditEvent<User, T> event) {
        try {
            dao.update(event.getRowValue());
        } catch (SQLException ex) {
            ex.printStackTrace();
            createDatabaseErrorAlert();
        }
    }

    /**
     * refreshes table by deleting all entries from the table view content, loading them and
     * refreshing the table view
     */
    private void refreshTable() {
        readAllDataAndShowInTableView();
        tableView.refresh();
    }

    @Override
    public String getWindowTitle() {
        return "Alle Benutzer";
    }

    @Override
    public boolean isClosingAppOnX() {
        return false;
    }

    @Override
    public String getFxmlPath() {
        return "/AllUserView.fxml";
    }

    @Override
    public PermissionKey getPermissionKey() {
        return PermissionKey.SHOW_ALL_USERS;
    }

    /**
     * unfortunately unlike a plain combobox, which can store entire objects in comboboxes and just show a
     * representation of an object, we have to pass a string, the gui representation, into the box and get it by
     * checking the gui representation of every Group-Object
     */
    private class GroupStringConverter extends StringConverter<Group> {

        @Override
        public String toString(Group group) {
            return group.getGuiRepresentation();
        }

        @Override
        public Group fromString(String s) {
            Optional<Group> group = Arrays.stream(GroupFactory.getInstance().getAllGroups())
                    .filter(stringGroup -> stringGroup.getGuiRepresentation().equals(s))
                    .findFirst();

            if (group.isPresent()) {
                return group.get();
            }
            throw new IllegalArgumentException(String.format("The group '%s' does not exist", s));
        }
    }
}
