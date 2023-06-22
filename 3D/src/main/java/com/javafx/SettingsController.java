package com.javafx;

import java.io.IOException;

import javafx.fxml.FXML;

public class SettingsController {
    @FXML
    private void goToStartMenu () throws IOException {
        StartPage.setRoot("startMenu");
    }

    @FXML
    private void goBackScene() throws IOException {
        
    }
}
