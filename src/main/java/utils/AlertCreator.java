package utils;

import javafx.scene.control.Alert;

/**
 * Util class to create JavaFX alerts
 *
 * @see Alert
 */
public class AlertCreator {
    /**
     * creates alert with the status <code>AlertType.INFORMATION</code>
     *
     * @return information alert
     */
    public static Alert createInformation(String title, String headerText, String contentText) {
        return createAlert(title, headerText, contentText, Alert.AlertType.INFORMATION);
    }

    /**
     * creates alert with the status <code>AlertType.WARNING</code>
     *
     * @return warning alert
     */
    public static Alert createWarning(String title, String headerText, String contentText) {
        return createAlert(title, headerText, contentText, Alert.AlertType.WARNING);
    }

    /**
     * creates alert with the status <code>AlertType.ERROR</code>
     *
     * @return error alert
     */
    public static Alert createError(String title, String headerText, String contentText) {
        return createAlert(title, headerText, contentText, Alert.AlertType.ERROR);
    }

    /**
     * Creates a JavaFX Alert
     *
     * @param title title that is used on the window frame
     * @param headerText header of the message
     * @param contentText content of the message
     * @param alertType the alert type see {@link Alert.AlertType}
     * @return JavaFX Alert
     */
    public static Alert createAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        if (title != null) {
            alert.setTitle(title);
        }
        if (headerText != null) {
            alert.setHeaderText(headerText);
        }
        if (contentText != null) {
            alert.setContentText(contentText);
        }
        return alert;
    }
}
