package com.javafx;

import java.io.IOException;

import javafx.fxml.FXML;

public class SettingsController {
    private boolean cameFromStartMenu;

    public SettingsController(boolean cameFromStartMenu) {
        this.cameFromStartMenu = cameFromStartMenu;
    }

    @FXML
    private void goToStartMenu() throws IOException {
        StartPage.setScene("startMenu");
    }

    @FXML
    private void goBackScene() throws IOException {
        if (cameFromStartMenu) {
            StartPage.setScene("startMenu");
        } else {
            FlightSimulatorGame game = new FlightSimulatorGame();
            StartPage.setScene(game.startGame());
        }
    }
}
