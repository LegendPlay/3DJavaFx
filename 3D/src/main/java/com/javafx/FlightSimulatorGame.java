package com.javafx;

import javafx.animation.AnimationTimer;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;

public class FlightSimulatorGame {
    // constants
    private static final int STAGE_WIDTH = 1440;
    private static final int STAGE_HEIGHT = 800;
    public static int tickRateFPS = 60;

    public Scene startGame(int seed) {
        Group group = new Group();
        Scene scene = new Scene(group, STAGE_WIDTH, STAGE_HEIGHT, true);

        // camera
        CameraHandler cameraHandler = new CameraHandler();
        Camera camera = cameraHandler.setupCamera();

        scene.setCamera(camera);
        scene.setFill(Color.LIGHTBLUE);

        // terrain
        Terrain terrain = new Terrain();
        MeshView mesh = terrain.generateTerrain(seed, cameraHandler);
        group.getChildren().add(mesh);

        // manage user input
        scene.addEventHandler(KeyEvent.KEY_PRESSED, cameraHandler::handleKeyPress);

        // main game ticks

        long tickDuration = (long) (1e9 / tickRateFPS); // tick rate to nanoseconds
        
        AnimationTimer animationTimer = new AnimationTimer() {
            private long previousTime = 0;

            @Override
            public void handle(long currentTime) {
                if (previousTime != 0) {
                    long deltaTime = currentTime - previousTime;

                    if (deltaTime >= tickDuration) {
                        cameraHandler.handleAnimationTick(deltaTime);
                        previousTime = currentTime;
                        // group.getChildren().add(terrain.generateTerrain(seed, cameraHandler));
                    }     
                } else {
                    previousTime = currentTime;
                }                       
            }
        };
        animationTimer.start();

        return scene;
    }
}
