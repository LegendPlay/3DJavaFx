package com.javafx;

import java.io.IOException;

import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;

public class CameraHandler {
    private static final double TRANSLATION_AMOUNT = 10.0;
    private static final double ROTATION_AMOUNT = 2.0;

    private Physics physics = new Physics(0);
    private Camera camera;
    private Rotate cameraRotationY;
    private Rotate cameraRotationX;
    private Rotate cameraRotationZ;
    private double coordinateX;
    private double altitude;
    private double coordinateZ;
    private boolean accelerate = false;
    private boolean decelerate = false;
    private byte curve = 0;
    private double rotY;
    private double rotX = 180;

    // key bindings
    private String keyFlyForward = SettingsHandler.getKeyBindingValue("Key-FlyForward");
    private String keyTurnLeft = SettingsHandler.getKeyBindingValue("Key-TurnLeft");
    private String keyTurnRight = SettingsHandler.getKeyBindingValue("Key-TurnRight");
    private String keyRotateDown = SettingsHandler.getKeyBindingValue("Key-RotateDown");
    private String keyRotateUp = SettingsHandler.getKeyBindingValue("Key-RotateUp");
    private String keySettingsMenu = SettingsHandler.getKeyBindingValue("Key-SettingsMenu");

    public Camera setupCamera() {
        camera = new PerspectiveCamera(true);

        cameraRotationY = new Rotate(0, Rotate.Y_AXIS);
        camera.getTransforms().add(cameraRotationY);
        cameraRotationX = new Rotate(0, Rotate.X_AXIS);
        camera.getTransforms().add(cameraRotationX);
        cameraRotationZ = new Rotate(0, Rotate.Z_AXIS);
        camera.getTransforms().add(cameraRotationZ);

        camera.setNearClip(10);
        camera.setFarClip(2000);
        coordinateX = 0;
        altitude = 20;
        coordinateZ = 0;

        cameraRotationX.setAngle(180);

        camera.setTranslateX(coordinateX);
        camera.setTranslateY(altitude);
        camera.setTranslateZ(coordinateZ);

        return camera;
    }

    public void handleKeyPress(KeyEvent event) {
        if (event.getCode().equals(KeyCode.valueOf(this.keyFlyForward))) {
            double deltaX = TRANSLATION_AMOUNT *
                    Math.sin(Math.toRadians(Math.abs(cameraRotationY.getAngle()) % 360));
            double deltaZ = TRANSLATION_AMOUNT *
                    Math.cos(Math.toRadians(Math.abs(cameraRotationY.getAngle()) % 360));
            coordinateX -= deltaX;
            coordinateZ -= deltaZ;

        } else if (event.getCode().equals(KeyCode.valueOf(this.keyTurnLeft))) {
            double deltaX = TRANSLATION_AMOUNT * Math.sin(Math.toRadians(cameraRotationY.getAngle() - 90) % 360);
            double deltaZ = TRANSLATION_AMOUNT * Math.cos(Math.toRadians(cameraRotationY.getAngle() - 90) % 360);
            coordinateX += deltaX;
            coordinateZ += deltaZ;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyTurnRight))) {
            double deltaX = TRANSLATION_AMOUNT * Math.sin(Math.toRadians(cameraRotationY.getAngle() + 90) % 360);
            double deltaZ = TRANSLATION_AMOUNT * Math.cos(Math.toRadians(cameraRotationY.getAngle() + 90) % 360);
            coordinateX += deltaX;
            coordinateZ += deltaZ;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyRotateDown))) {
            rotX -= ROTATION_AMOUNT;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyRotateUp))) {
            rotX += ROTATION_AMOUNT;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keySettingsMenu))) {
            try {
                StartPage.setSettingsScene("settingsMenu", "flightSimulator");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    public void handleAnimationTick(long timeBetweenTickInNano) {
        camera.setTranslateX(coordinateX);
        camera.setTranslateZ(coordinateZ);
        camera.setTranslateY(altitude);
        cameraRotationY.setAngle(rotY);
        cameraRotationX.setAngle(rotX);
    }

    /*
     * public void handleKeyPress(KeyEvent event) {
     * if (event.getCode().equals(KeyCode.valueOf(SettingsHandler.getProperty(
     * "Key-FlyForward")))) {
     * accelerate = true;
     * } else if
     * (event.getCode().equals(KeyCode.valueOf(SettingsHandler.getProperty(
     * "Key-Decelerate")))) {
     * decelerate = true;
     * } else if
     * (event.getCode().equals(KeyCode.valueOf(SettingsHandler.getProperty(
     * "Key-TurnLeft")))) {
     * curve = -1;
     * } else if
     * (event.getCode().equals(KeyCode.valueOf(SettingsHandler.getProperty(
     * "Key-TurnRight")))) {
     * curve = 1;
     * } else if
     * (event.getCode().equals(KeyCode.valueOf(SettingsHandler.getProperty(
     * "Key-RotateDown")))) {
     * physics.turnUp(-ROTATION_AMOUNT);
     * } else if
     * (event.getCode().equals(KeyCode.valueOf(SettingsHandler.getProperty(
     * "Key-RotateUp")))) {
     * physics.turnUp(ROTATION_AMOUNT);
     * } else if
     * (event.getCode().equals(KeyCode.valueOf(SettingsHandler.getProperty(
     * "Key-SettingsMenu")))) {
     * try {
     * StartPage.setSettingsScene("settingsMenu", false);
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * } else {
     * }
     * }
     * 
     * public void handleAnimationTick(long timeBetweenTickInNano) {
     * altitude = camera.getTranslateY();
     * double timeInSec = timeBetweenTickInNano * 1E-9;
     * 
     * if (accelerate) {
     * physics.accelerate(timeInSec);
     * accelerate = false;
     * } else if (decelerate) {
     * physics.decelerate(timeInSec);
     * decelerate = false;
     * }
     * 
     * if (curve != 0) {
     * cameraRotationZ.setAngle(physics.flyCurve(timeInSec, altitude, curve));
     * curve = 0;
     * } else {
     * cameraRotationZ.setAngle(0);
     * physics.sleep(timeInSec, altitude);
     * }
     * 
     * camera.setTranslateX(physics.getDeltaX(timeInSec));
     * camera.setTranslateZ(physics.getDeltaZ(timeInSec));
     * camera.setTranslateY(physics.getDeltaY(timeInSec));
     * cameraRotationY.setAngle(physics.getAngle());
     * cameraRotationX.setAngle(physics.getAngleDown());
     * }
     */

    public double getCoordinateX() {
        return this.coordinateX;
    }

    public double getCoordinateZ() {
        return this.coordinateZ;
    }

    public double getAltitude() {
        return this.altitude;
    }
}