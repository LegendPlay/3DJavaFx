package com.javafx;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartPageController {
    @FXML
    private Button startButton;

    @FXML
    private void startGame() {
        FlightSimulatorGame game = new FlightSimulatorGame();
        game.startGame((Stage) startButton.getScene().getWindow());
    }

    @FXML
    private void goToSettings() throws IOException {
        StartPage.setRoot("settingsMenu");
    }
}
