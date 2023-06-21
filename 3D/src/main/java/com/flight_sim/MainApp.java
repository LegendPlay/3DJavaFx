package com.flight_sim;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

// import static java.lang.Math.ceil;
import static java.lang.Math.floor;

public class MainApp extends Application {
    // constants
    public static final int STAGE_WIDTH = 1440;
    public static final int STAGE_HEIGHT = 800;

    @Override
    public void start(Stage stage) throws IOException {
        // change to set number of boxes per row
        Group group = new Group();
        Scene scene = new Scene(group, STAGE_WIDTH, STAGE_HEIGHT, true);
        CameraHandler camera = new CameraHandler();

        Terrain terrain = new Terrain(group, 100, camera);

        // manages the user inputs
        stage.addEventHandler(KeyEvent.KEY_PRESSED, camera::handleKeyPress);

        // makes stage displayable
        scene.setFill(Color.LIGHTBLUE);
        stage.setTitle("Flight Simulator");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

        // main game ticks
        /* AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                camera.handleAnimationTick();
            }
        };
        animationTimer.start(); */
    }

    public static void main(String[] args) {
        launch();
    }
}