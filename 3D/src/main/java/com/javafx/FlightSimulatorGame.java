package com.javafx;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class FlightSimulatorGame {
    // constants
    private static final int STAGE_WIDTH = 1440;
    private static final int STAGE_HEIGHT = 800;

    public void startGame(Stage stage) {
        Group group = new Group();
        Scene scene = new Scene(group, STAGE_WIDTH, STAGE_HEIGHT, true);

        CameraHandler camera = new CameraHandler();
        camera.setupCamera(scene);

        // terrain
        Terrain terrain = new Terrain(group, 400, camera);

        // manage user input
        scene.addEventHandler(KeyEvent.KEY_PRESSED, camera::handleKeyPress);

        // main game ticks
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                camera.handleAnimationTick();
            }
        };
        animationTimer.start();

        stage.setScene(scene);
    }
}
