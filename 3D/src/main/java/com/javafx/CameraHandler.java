package com.javafx;

import java.io.IOException;

import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;

public class CameraHandler {
    private static final double ROTATION_AMOUNT = 2.0;

    private double rotX = 180;
    private double rotY = 0;
    private double rotZ = 0;
    private Physics physics = new Physics(0);
    private Camera camera;
    private Rotate cameraRotationY;
    private Rotate cameraRotationX;
    private Rotate cameraRotationZ;
    private double coordinateX;
    private double altitude;
    private double coordinateZ;
    private byte curve = 0;

    // key bindings
    private String keyFlyForward = SettingsHandler.getKeyBindingValue("Key-FlyForward");
    private String keyDecelerate = SettingsHandler.getKeyBindingValue("Key-Decelerate");
    private String keyTurnLeft = SettingsHandler.getKeyBindingValue("Key-TurnLeft");
    private String keyTurnRight = SettingsHandler.getKeyBindingValue("Key-TurnRight");
    private String keyRotateDown = SettingsHandler.getKeyBindingValue("Key-RotateDown");
    private String keyRotateUp = SettingsHandler.getKeyBindingValue("Key-RotateUp");
    private String keySettingsMenu = SettingsHandler.getKeyBindingValue("Key-SettingsMenu");

    private int world_id;

    public CameraHandler(int world_id) {
        this.world_id = world_id;
    }

    public Camera setupCamera(double position_x, double position_y, double position_z, double rotation_x,
            double rotation_y, double rotation_z) {
        camera = new PerspectiveCamera(true);

        cameraRotationX = new Rotate(0, Rotate.X_AXIS);
        cameraRotationY = new Rotate(0, Rotate.Y_AXIS);
        cameraRotationZ = new Rotate(0, Rotate.Z_AXIS);
        camera.getTransforms().addAll(cameraRotationX, cameraRotationY, cameraRotationZ);

        // render distance
        camera.setNearClip(10);
        camera.setFarClip(2000);

        // rotations
        rotX = rotation_x;
        rotY = rotation_y;
        rotZ = rotation_z;

        // positions
        coordinateX = position_x;
        altitude = position_y;
        coordinateZ = position_z;

        camera.setTranslateX(coordinateX);
        camera.setTranslateY(altitude);
        camera.setTranslateZ(coordinateZ);

        return camera;
    }

    public void handleKeyPress(KeyEvent event) {
        if (event.getCode().equals(KeyCode.valueOf(this.keyFlyForward))) {
            double deltaX = 10 *
                    Math.sin(Math.toRadians(Math.abs(cameraRotationY.getAngle()) % 360));
            double deltaZ = 10 *
                    Math.cos(Math.toRadians(Math.abs(cameraRotationY.getAngle()) % 360));
            coordinateX -= deltaX;
            coordinateZ -= deltaZ;

        } else if (event.getCode().equals(KeyCode.valueOf(this.keyTurnLeft))) {
            double deltaX = 10 * Math.sin(Math.toRadians(cameraRotationY.getAngle() - 90) % 360);
            double deltaZ = 10 * Math.cos(Math.toRadians(cameraRotationY.getAngle() - 90) % 360);
            coordinateX += deltaX;
            coordinateZ += deltaZ;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyTurnRight))) {
            double deltaX = 10 * Math.sin(Math.toRadians(cameraRotationY.getAngle() + 90) % 360);
            double deltaZ = 10 * Math.cos(Math.toRadians(cameraRotationY.getAngle() + 90) % 360);
            coordinateX += deltaX;
            coordinateZ += deltaZ;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyRotateDown))) {
            rotX -= ROTATION_AMOUNT;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyRotateUp))) {
            rotX += ROTATION_AMOUNT;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keySettingsMenu))) {
            try {
                StartPage.setSettingsScene("settingsMenu", "false", 0);// TODO change world id
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
        /* if (event.getCode().equals(KeyCode.valueOf(this.keyFlyForward))) {
            physics.accelerate();
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyTurnLeft))) {
            curve = -1;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyTurnRight))) {
            curve = 1;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyDecelerate))) {
            physics.decelerate();
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyRotateDown))) {
            // rotX -= ROTATION_AMOUNT;
            physics.turnUp(-ROTATION_AMOUNT);
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyRotateUp))) {
            // rotX += ROTATION_AMOUNT;
            physics.turnUp(ROTATION_AMOUNT);
        } else if (event.getCode().equals(KeyCode.valueOf(this.keySettingsMenu))) {
            try {
                StartPage.setSettingsScene("settingsMenu", "flightSimulator", world_id);

                SettingsHandler.saveGameData(world_id, coordinateX, altitude, coordinateZ, rotX, rotY, rotZ);
                System.out.println("Game saved!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        } */
    }

    public void handleAnimationTick(long timeBetweenTickInNano) {
        if (curve != 0) {
            cameraRotationZ.setAngle(physics.flyCurve(altitude, curve));
            curve = 0;
        } else {
            cameraRotationZ.setAngle(0);
            physics.sleep(altitude);
        }
        camera.setTranslateX(camera.getTranslateX() + physics.getDeltaX());
        camera.setTranslateZ(camera.getTranslateZ() + physics.getDeltaZ());
        camera.setTranslateY(camera.getTranslateY() + physics.getDeltaY());
        cameraRotationY.setAngle(physics.getAngle());
        cameraRotationX.setAngle(physics.getAngleDown());
    }

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