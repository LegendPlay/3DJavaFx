package com.javafx;

import java.io.IOException;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartPageController {
    private int randomSeed = new Random().nextInt(10000);
    @FXML
    private Button newGameButton;

    @FXML
    private void goToSavedWorlds() {

    }

    @FXML
    private void createNewWorld() throws IOException {
        FlightSimulatorGame game = new FlightSimulatorGame();
        StartPage.setScene(game.startGame(randomSeed));
        SettingsHandler.put("seed", String.valueOf(randomSeed));
    }

    @FXML
    private void goToSettings() throws IOException {
        StartPage.setSettingsScene("settingsMenu", true);
    }
}
