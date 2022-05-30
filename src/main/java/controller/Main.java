package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage loginStage;

    @Override
    public void start(Stage loginStage) {
        this.loginStage = loginStage;
        executeLoginWindow();
    }

    public void executeLoginWindow() {
        try {
            createLoginWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLoginWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/LoginView.fxml"));
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);

        LoginController controller = loader.getController();
        controller.setStage(this.loginStage);

        this.loginStage.setTitle("NHPlus - Login");
        this.loginStage.setScene(scene);
        this.loginStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}