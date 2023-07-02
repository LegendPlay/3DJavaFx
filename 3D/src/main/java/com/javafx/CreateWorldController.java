package com.javafx;

import java.io.IOException;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class CreateWorldController {
    private int randomSeed = new Random().nextInt(10000);

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void goBackScene() throws IOException {
        StartPage.setScene("startMenu");
    }

    @FXML
    private void createWorld() {
        FlightSimulatorGame game = new FlightSimulatorGame();
        StartPage.setScene(game.startGame(randomSeed));

        // SettingsHandler.put("seed", String.valueOf(randomSeed)); // TODO seed
    }

    @FXML
    private void goToSettings() throws IOException {
        StartPage.setSettingsScene("settingsMenu", "createWorldMenu");
    }

    @FXML
    private void onPressedAnchorPane() {
        anchorPane.requestFocus();
    }
}
