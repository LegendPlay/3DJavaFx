package com.javafx;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartPage extends Application {
    private static Scene scene;
    private static Stage stage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        StartPage.stage = stage;

        scene = new Scene(loadFXML("startMenu"), 1440, 800);

        stage.setTitle("Flight Simulator");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    static void setScene(Scene scene) {
        stage.setScene(scene);
    }

    static void setScene(String fxml) throws IOException {
        Scene scene = new Scene(loadFXML(fxml));
        stage.setScene(scene);
    }

    static void setSettingsScene(String fxml, boolean cameFromStartMenu) throws IOException {
        FXMLLoader loader = new FXMLLoader(StartPage.class.getResource("/fxml/" + fxml + ".fxml"));
        loader.setController(new SettingsController(cameFromStartMenu));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    // static void setRoot(String fxml) throws IOException {
    // scene.setRoot(loadFXML(fxml));
    // }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
