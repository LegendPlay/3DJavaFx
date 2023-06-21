package com.flight_sim;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

public class MainApp extends Application {
    // constants
    public static final int WIDTH = 1440;
    public static final int HEIGHT = 800;

    @Override
    public void start(Stage stage) throws IOException {
        // change to set number of boxes per row
        int boxesperrow = 255;
        int boxes = boxesperrow * boxesperrow;

        // creating new array for the landscape to be stored
        Box[] cubes;
        cubes = new Box[boxes];

        // creating the group that is later added to the scene
        Group group = new Group();

        // materials
        final PhongMaterial grass = new PhongMaterial();
        grass.setSpecularColor(Color.LIGHTGREEN);
        grass.setDiffuseColor(Color.GREEN);

        // creating the landscape
        for (int i = 0; i < boxes; i++) {

            cubes[i] = new Box(50, ceil(Math.random() * 150), 50);
            cubes[i].translateXProperty().set(i % Math.sqrt((double) boxes) * 50);
            cubes[i].translateZProperty().set(floor((double) i / Math.sqrt((double) boxes)) * 50);
            cubes[i].setMaterial(grass);
            group.getChildren().add(cubes[i]);

        }

        Camera camera = new PerspectiveCamera(true);

        // creating the scene
        Scene scene = new Scene(group, WIDTH, HEIGHT, true);
        scene.setFill(Color.LIGHTBLUE);
        scene.setCamera(camera);

        // setting up the camera
        camera.translateZProperty().set(-500);
        camera.setNearClip(20);
        camera.setFarClip(2000);
        camera.translateXProperty().set(floor((double) boxesperrow * 50 / 2));
        camera.translateYProperty().set(-250);
        camera.translateZProperty().set(50);

        // manages the user inputs
        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case A:
                    camera.translateXProperty().set(camera.getTranslateX() - 10);
                    break;
                case D:
                    camera.translateXProperty().set(camera.getTranslateX() + 10);
                    break;
                case W:
                    camera.translateYProperty().set(camera.getTranslateY() - 5);
                    break;
                case S:
                    camera.translateYProperty().set(camera.getTranslateY() + 5);
                    break;
                case LEFT:
                    camera.rotateProperty().set(camera.getRotate() - 2);
                    break;
                case RIGHT:
                    camera.rotateProperty().set(camera.getRotate() + 2);
                    break;
                default:
                    break;
            }
        });

        // makes stage displayable
        stage.setTitle("Flight Simulator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // main game ticks
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                // smooth movement
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), camera);
                camera.translateZProperty().set(camera.getTranslateZ() + 5);
                translateTransition.play();
            }
        };
        animationTimer.start();
    }

    public static void main(String[] args) {
        launch();
    }
}