package com.javafx;

import java.io.IOException;

import javafx.fxml.FXML;

public class StartPageController {

    @FXML
    private void goToSavedWorlds() throws IOException {
        StartPage.setScene("savedWorldsMenu");
    }

    @FXML
    private void quitGame() {
        StartPage.closeStage();
    }

    @FXML
    private void createNewWorld() throws IOException {
        StartPage.setScene("createWorldMenu");
    }

    @FXML
    private void goToSettings() throws IOException {
        StartPage.setSettingsScene("settingsMenu", "startPage", FlightSimulatorGame.world_id);
    }
}
