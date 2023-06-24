package com.javafx;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartPageController {
    @FXML
    private Button startButton;

    @FXML
    private void startGame() throws IOException {
        FlightSimulatorGame game = new FlightSimulatorGame();
        StartPage.setScene(game.startGame());
    }

    @FXML
    private void goToSettings() throws IOException {
        StartPage.setSettingsScene("settingsMenu", true);
    }
}
