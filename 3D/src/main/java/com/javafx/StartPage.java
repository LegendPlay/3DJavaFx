package com.javafx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StartPage extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        StartPage.stage = stage;

        SettingsHandler.initializeDatabase();

        Scene scene = new Scene(loadFXML("startMenu"), 1440, 800);

        stage.setOnCloseRequest(this::onWindowClose);

        stage.setTitle("Flight Simulator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void onWindowClose(WindowEvent event) {
        // show alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Game");
        alert.setHeaderText("Do you want to save the game before closing?");
        alert.setContentText("Choose your option.");

        ButtonType saveButton = new ButtonType("Save");
        ButtonType closeButton = new ButtonType("Close without saving");
        ButtonType cancelButton = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(saveButton, closeButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == saveButton) {
                // save the game logic here
                System.out.println("Game Saved!");
            } else if (response == closeButton) {
                // user chose to close the window without saving
                event.consume();
                stage.close();
            } else {
                // user chose to cancel the close request
                event.consume();
            }
        });
    }

    public static void closeStage() {
        stage.close();
    }

    public static void setScene(Scene scene) {
        stage.setScene(scene);
    }

    public static void setScene(String fxml) throws IOException {
        Scene scene = new Scene(loadFXML(fxml));
        stage.setScene(scene);
    }

    public static void setSettingsScene(String fxml, String cameFromMenu, int world_id) throws IOException {
        FXMLLoader loader = new FXMLLoader(StartPage.class.getResource("/fxml/" + fxml + ".fxml"));
        loader.setController(new SettingsController(cameFromMenu, world_id));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
