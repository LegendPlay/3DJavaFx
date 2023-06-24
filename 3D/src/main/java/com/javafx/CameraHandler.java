package com.javafx;

import java.io.IOException;

import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;

public class CameraHandler {
    private static final double TRANSLATION_AMOUNT = 10.0;
    private static final double ROTATION_AMOUNT = 2.0;

    private Camera camera;
    private Rotate cameraRotation;
    private double cameraTranslateX;
    private double cameraTranslateY;
    private double cameraTranslateZ;

    public Camera setupCamera() {
        camera = new PerspectiveCamera(true);

        cameraRotation = new Rotate(0, Rotate.Y_AXIS);
        camera.getTransforms().add(cameraRotation);

        camera.setNearClip(10);
        camera.setFarClip(2000);
        cameraTranslateX = 0;
        cameraTranslateY = 10;
        cameraTranslateZ = 0;

        camera.setTranslateX(cameraTranslateX);
        camera.setTranslateY(cameraTranslateY);
        camera.setTranslateZ(cameraTranslateZ);

        return camera;
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
                cameraTranslateZ += TRANSLATION_AMOUNT;
                break;
            case S:
                cameraTranslateZ -= TRANSLATION_AMOUNT;
                break;
            case R:
                cameraTranslateY -= TRANSLATION_AMOUNT;
                break;
            case F:
                cameraTranslateY += TRANSLATION_AMOUNT;
                break;
            case LEFT:
                cameraRotation.setAngle(cameraRotation.getAngle() - ROTATION_AMOUNT);
                break;
            case RIGHT:
                cameraRotation.setAngle(cameraRotation.getAngle() + ROTATION_AMOUNT);
                break;
            case ESCAPE:
                try {
                    StartPage.setSettingsScene("settingsMenu", false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
                break;
        }

        camera.setTranslateX(cameraTranslateX);
        camera.setTranslateZ(cameraTranslateZ);
        camera.setTranslateY(cameraTranslateY);
    }

    public void handleAnimationTick() {
        // TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), camera);
        // // TODO move forward temporary
        // cameraTranslateZ = camera.getTranslateZ() + 5;
        // translateTransition.play();

        // camera.setTranslateZ(cameraTranslateZ); 
    }

    public double getCameraTranslateX() {
        return cameraTranslateX;
    }
    public double getCameraTranslateY() {
        return cameraTranslateY;
    }
}