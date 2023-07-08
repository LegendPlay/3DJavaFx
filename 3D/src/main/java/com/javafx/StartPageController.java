package com.javafx;

import java.io.IOException;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class StartPageController {
    
    @FXML
    private ImageView backgroundImage;

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

    public void initialize() {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(60), backgroundImage);
        translateTransition.setFromX(0);
        translateTransition.setToX(-2500);
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setCycleCount(Timeline.INDEFINITE);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }
}
