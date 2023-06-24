package com.javafx;

public class Physics {
    // the smaller the time used for the calculations the more precise and accurate
    // the results (higher update frequency of velocity)
    private static final double GRAVITATIONAL_ACCELERATION = 9.8067;
    private static final double DRAG_COEFFICIENT_FLIGHT = 0.012;
    private static final double AIR_DENSITY = 1.225;
    private static final double WING_AREA = 525.0;
    private static final double MAX_BANK_ANGLE = 66.5;
    private static final int MASS = 396893;
    private static final byte TURN_PER_SECOND = 3;
    private double velocityY = 0;
    private double velocityX = 0;
    private double velocityZ = 0;
    private double velocity = 0;
    private double angle;

    Physics(double angle) {
        this.angle = angle;
    }

    public void sleep(double time) {
        velocity -= calcDrag() * time / MASS;
        velocityX = velocity * Math.sin(angle);
        velocityZ = velocity * Math.cos(angle);
    }

    private double calcDrag() {
        return 0.5 * Math.pow(velocity, 2) * DRAG_COEFFICIENT_FLIGHT * WING_AREA * AIR_DENSITY;
    }

    public void turn(double angle) {// changes angle
        this.angle += angle;
        // keep the angle between 0 and 360 degrees (for convenience)
        angle += angle < 0 ? 360 : angle > 360 ? (-360) : 0;
    }

    public double turn(double angle, double time) {// changes angle and simulates resulting rotation, speed changes...
        // returns angle of plane (angle around plane body and not coordinate axis)
        // only for fast, approximated results
        turn(angle);
        return angle / (360 * time) * 45;
    }

    // getters for distance

    public double getDeltaX(double time) {
        return time * velocityX;
    }

    public double getDeltaZ(double time) {
        return time * velocityZ;
    }

    public double getDeltaY(double time) {
        return time * velocityY;
    }

    // getter and setter methods
    public double getVelocityZ() {
        return velocityZ;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
