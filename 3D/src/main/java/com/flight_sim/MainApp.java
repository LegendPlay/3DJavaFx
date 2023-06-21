package com.flight_sim;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    // constants
    private static final int STAGE_WIDTH = 1440;
    private static final int STAGE_HEIGHT = 800;

    @Override
    public void start(Stage stage) throws IOException {
        // creating the group that is added to the scene
        Group group = new Group();
        Scene scene = new Scene(group, STAGE_WIDTH, STAGE_HEIGHT, true);

        // terrain
        TerrainGeneration terrainGeneration = new TerrainGeneration();
        terrainGeneration.createTerrain(group, TerrainGeneration.BOXES_PER_ROW);

        CameraHandler camera = new CameraHandler();
        camera.setupCamera(scene, TerrainGeneration.BOXES_PER_ROW);

        // manages the user inputs
        stage.addEventHandler(KeyEvent.KEY_PRESSED, camera::handleKeyPress);

        // makes stage displayable
        stage.setTitle("Flight Simulator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // main game ticks
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                camera.handleAnimationTick();
            }
        };
        animationTimer.start();
    }

    public static void main(String[] args) {
        launch();
    }
}