package com.javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class SettingsController implements Initializable{
    private boolean cameFromStartMenu;

    @FXML
    AnchorPane anchorPane;

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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        anchorPane.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                try {
                    goBackScene();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }); 
    }
}
