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

    public static int world_id;
    private int seed;
    private double position_x;
    private double position_y;
    private double position_z;
    private double rotation_x;
    private double rotation_y;
    private double rotation_z;
    private boolean isInFreeFlyMode;

    public FlightSimulatorGame(GameData gameData) {
        FlightSimulatorGame.world_id = gameData.getWorldId();
        this.seed = gameData.getSeed();
        this.position_x = gameData.getPositionX();
        this.position_y = gameData.getPositionY();
        this.position_z = gameData.getPositionZ();
        this.rotation_x = gameData.getRotationX();
        this.rotation_y = gameData.getRotationY();
        this.rotation_z = gameData.getRotationZ();
        this.isInFreeFlyMode = gameData.getIsInFreeFlyMode();
    }

    public Scene startGame() {
        Group group = new Group();
        Scene scene = new Scene(group, STAGE_WIDTH, STAGE_HEIGHT, true);

        if (isInFreeFlyMode) {
            CameraHandlerFreeFlyMode cameraHandler = new CameraHandlerFreeFlyMode(world_id);

            return game(cameraHandler, scene, group);
        } else {
            CameraHandlerPhysics cameraHandler = new CameraHandlerPhysics(world_id);

            return game(cameraHandler, scene, group);
        }
    }

    private Scene game(CameraHandler cameraHandler, Scene scene, Group group) {

        Camera camera = cameraHandler.setupCamera(position_x, position_y, position_z, rotation_x, rotation_y,
                rotation_z);

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
