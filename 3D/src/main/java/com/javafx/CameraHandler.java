package com.javafx;

import javafx.scene.Camera;
import javafx.scene.input.KeyEvent;

public abstract class CameraHandler {
    public abstract double getCoordinateX();

    public abstract double getAltitude();

    public abstract Camera setupCamera(double position_x, double position_y, double position_z, double rotation_x,
            double rotation_y, double rotation_z);

    public abstract void handleAnimationTick(long timeBetweenTickInNano);

    public abstract void handleKeyPress(KeyEvent event);
}
