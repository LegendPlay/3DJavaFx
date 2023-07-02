package com.javafx;

import java.io.IOException;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class CreateWorldController {
    private final String pattern = "([1-9]\\d{0,3}?|10000)";

    @FXML
    private TextField seedTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void goBackScene() throws IOException {
        StartPage.setScene("startMenu");
    }

    @FXML
    private void createWorld() {
        boolean nameValid = false;
        boolean seedValid = false;

        String worldName = nameTextField.getText();
        String seedText = seedTextField.getText();
        int userSeed = 0;

        if (worldName.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Name");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid name for this world");
            alert.showAndWait();
        } else {
            nameValid = true;
        }

        if (seedText.matches(pattern)) {
            userSeed = Integer.parseInt(seedText);
            seedValid = true;
        } else if (seedText.equals("")) {
            userSeed = new Random().nextInt(10000);
            seedValid = true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Seed");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a number between 1 and 10000 as a seed.");
            alert.showAndWait();

            seedTextField.clear();
        }

        if (nameValid && seedValid) {
            SettingsHandler.createGame(userSeed, worldName);

            GameData data = SettingsHandler.readGameData(SettingsHandler.getNewestWorldId());

            FlightSimulatorGame game = new FlightSimulatorGame(data);
            StartPage.setScene(game.startGame());
        }

    }

    @FXML
    private void goToSettings() throws IOException {
        StartPage.setSettingsScene("settingsMenu", "createWorldMenu", FlightSimulatorGame.world_id);
    }

    @FXML
    private void onPressedAnchorPane() {
        anchorPane.requestFocus();
    }
}
