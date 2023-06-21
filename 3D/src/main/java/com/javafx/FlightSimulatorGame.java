package com.javafx;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class FlightSimulatorGame {
    // constants
    private static final int STAGE_WIDTH = 1440;
    private static final int STAGE_HEIGHT = 800;

    public void startGame(Stage stage) {
        // create group that is added to the scene
        Group group = new Group();
        Scene scene = new Scene(group, STAGE_WIDTH, STAGE_HEIGHT, true);

        // terrain
        TerrainGeneration terrainGeneration = new TerrainGeneration();
        terrainGeneration.createTerrain(group);

        // camera
        CameraHandler camera = new CameraHandler();
        camera.setupCamera(scene);

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