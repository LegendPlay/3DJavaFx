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
    private Rotate cameraRotationY;
    private Rotate cameraRotationX;
    private double cameraTranslateX;
    private double cameraTranslateY;
    private double cameraTranslateZ;
    private double deltaX;
    private double deltaZ;
    private double rotY;
    private double rotX = 180;

    public Camera setupCamera() {
        camera = new PerspectiveCamera(true);

        cameraRotationY = new Rotate(0, Rotate.Y_AXIS);
        camera.getTransforms().add(cameraRotationY);
        cameraRotationX = new Rotate(0, Rotate.X_AXIS);
        camera.getTransforms().add(cameraRotationX);

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
                deltaX = TRANSLATION_AMOUNT * Math.sin(Math.toRadians(cameraRotationY.getAngle() - 90) % 360);
                deltaZ = TRANSLATION_AMOUNT * Math.cos(Math.toRadians(cameraRotationY.getAngle() - 90) % 360);
                cameraTranslateX += deltaX;
                cameraTranslateZ += deltaZ;

                break;
            case D:
                deltaX = TRANSLATION_AMOUNT * Math.sin(Math.toRadians(cameraRotationY.getAngle() + 90) % 360);
                deltaZ = TRANSLATION_AMOUNT * Math.cos(Math.toRadians(cameraRotationY.getAngle() + 90) % 360);
                cameraTranslateX += deltaX;
                cameraTranslateZ += deltaZ;
                break;
            case W:
                deltaX = TRANSLATION_AMOUNT * Math.sin(Math.toRadians(cameraRotationY.getAngle()) % 360);
                deltaZ = TRANSLATION_AMOUNT * Math.cos(Math.toRadians(cameraRotationY.getAngle()) % 360);
                cameraTranslateX += deltaX;
                cameraTranslateZ += deltaZ;
                break;
            case S:
                deltaX = TRANSLATION_AMOUNT * Math.sin(Math.toRadians(Math.abs(cameraRotationY.getAngle()) % 360));
                deltaZ = TRANSLATION_AMOUNT * Math.cos(Math.toRadians(Math.abs(cameraRotationY.getAngle()) % 360));
                cameraTranslateX -= deltaX;
                cameraTranslateZ -= deltaZ;
                break;
            case SPACE:
                cameraTranslateY -= TRANSLATION_AMOUNT;
                break;
            case C:
                cameraTranslateY += TRANSLATION_AMOUNT;
                break;
            case LEFT:
                rotY -= ROTATION_AMOUNT;
                break;
            case RIGHT:
                rotY += ROTATION_AMOUNT;
                break;
            case UP:
                rotX += ROTATION_AMOUNT;
                break;
            case DOWN:
                rotX -= ROTATION_AMOUNT;
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
    }

    public void handleAnimationTick(long timeBetweenTickInNano) {
        camera.setTranslateX(cameraTranslateX);
        camera.setTranslateZ(cameraTranslateZ);
        camera.setTranslateY(cameraTranslateY);
        cameraRotationY.setAngle(rotY);
        cameraRotationX.setAngle(rotX);
    }

    public double getCameraTranslateX() {
        return cameraTranslateX;
    }

    public double getCameraTranslateY() {
        return cameraTranslateY;
    }
}
