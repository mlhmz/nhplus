package controller;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage loginStage) {
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
        ControllerManager.getInstance().getLoginStage().show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}