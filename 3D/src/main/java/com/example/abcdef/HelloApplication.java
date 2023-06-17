package com.example.abcdef;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

public class HelloApplication extends Application {
    //constants
    public static final int WIDTH = 1440;
    public static final int HEIGHT = 800;

    @Override
    public void start(Stage stage) throws IOException {
        //change to set number of boxes per row
        int boxesperrow = 255;
        int boxes = boxesperrow * boxesperrow;

        //creating new array for the landscape to be stored
        Box[] cubes;
        cubes = new Box[boxes];


        //creating the group that is later added to the scene
        Group group = new Group();

        //materials
        final PhongMaterial grass = new PhongMaterial();
        grass.setSpecularColor(Color.LIGHTGREEN);
        grass.setDiffuseColor(Color.GREEN);

        //creating the landscape
        for (int i = 0; i < boxes; i++) {

            cubes[i] = new Box(50, ceil(Math.random() * 150), 50);
            cubes[i].translateXProperty().set(i % Math.sqrt((double) boxes) * 50);
            cubes[i].translateZProperty().set(floor((double) i / Math.sqrt((double) boxes)) * 50);
            cubes[i].setMaterial(grass);
            group.getChildren().add(cubes[i]);

        }


        Camera camera = new PerspectiveCamera(true);

        //creating the scene
        Scene scene = new Scene(group, WIDTH, HEIGHT,true);
        scene.setFill(Color.LIGHTBLUE);
        scene.setCamera(camera);

        //setting up the camera
        camera.translateZProperty().set(-500);
        camera.setNearClip(20);
        camera.setFarClip(2000);
        camera.translateXProperty().set(floor((double) boxesperrow * 50 / 2));
        camera.translateYProperty().set(-250);
        camera.translateZProperty().set(50);

        //manages the user inputs
        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case A:
                    camera.translateXProperty().set(camera.getTranslateX() - 10);
                    break;
                case D:
                    camera.translateXProperty().set(camera.getTranslateX() + 10);
                    break;
                case W:
                    camera.translateYProperty().set(camera.getTranslateY() - 3);
                    break;
                case S:
                    camera.translateYProperty().set(camera.getTranslateY() + 6);
                    break;
                case LEFT:
                    camera.rotateProperty().set(camera.getRotate() - 2);
                    break;
                case RIGHT:
                    camera.rotateProperty().set(camera.getRotate() + 2);
                    break;

            }
        });

        //makes stage displayable
        stage.setTitle("3D");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        //runs  the task every 10ms
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                camera.translateZProperty().set(camera.getTranslateZ() + 5);
            }

        };
        timer.scheduleAtFixedRate(task, 0, 10);


    }


    public static void main(String[] args) {
        launch();
    }
}