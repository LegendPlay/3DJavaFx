package com.javafx;

import java.io.IOException;

import javafx.fxml.FXML;

public class SavedWorldsController {
    @FXML
    private void goToSettings() throws IOException {
        StartPage.setSettingsScene("settingsMenu", "savedWorldsMenu", FlightSimulatorGame.world_id);
    }

    @FXML
    private void goBackScene() throws IOException {
        StartPage.setScene("startMenu");
    }
}
