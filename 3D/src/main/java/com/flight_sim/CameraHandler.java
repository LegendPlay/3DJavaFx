package com.flight_sim;

import javafx.animation.TranslateTransition;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import static java.lang.Math.floor;

public class CameraHandler {
    private Camera camera;
    private Rotate cameraRotation;
    private double cameraTranslateX;
    private double cameraTranslateY;
    private double cameraTranslateZ;

    private static final double TRANSLATION_AMOUNT = 10.0;
    private static final double ROTATION_AMOUNT = 2.0;

    public void setupCamera(Scene scene, int boxesPerRow) {
        camera = new PerspectiveCamera(true);

        cameraRotation = new Rotate(0, Rotate.Y_AXIS);
        camera.getTransforms().add(cameraRotation);

        // creating the scene
        scene.setFill(Color.LIGHTBLUE);
        scene.setCamera(camera);

        // setting up the camera
        camera.setNearClip(20);
        camera.setFarClip(2000);
        cameraTranslateX = 0;
        cameraTranslateY = -250;
        cameraTranslateZ = 0;

        camera.setTranslateX(cameraTranslateX);
        camera.setTranslateY(cameraTranslateY);
        camera.setTranslateZ(cameraTranslateZ);
    }

    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case A:
                cameraTranslateX -= TRANSLATION_AMOUNT;
                break;
            case D:
                cameraTranslateX += TRANSLATION_AMOUNT;
                break;
            case W:
                cameraTranslateY -= TRANSLATION_AMOUNT;
                break;
            case S:
                cameraTranslateY += TRANSLATION_AMOUNT;
                break;
            case LEFT:
                cameraRotation.setAngle(cameraRotation.getAngle() - ROTATION_AMOUNT);
                break;
            case RIGHT:
                cameraRotation.setAngle(cameraRotation.getAngle() + ROTATION_AMOUNT);
                break;
            default:
                break;
        }

        camera.setTranslateX(cameraTranslateX);
        camera.setTranslateY(cameraTranslateY);
    }

    public void handleAnimationTick() {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), camera);
        cameraTranslateZ = camera.getTranslateZ() + 5;
        translateTransition.play();

        camera.setTranslateZ(cameraTranslateZ);
    }

    public double getCameraTranslateX() {
        return cameraTranslateX;
    }

    public double getCameraTranslateY() {
        return cameraTranslateY;
    }
}
