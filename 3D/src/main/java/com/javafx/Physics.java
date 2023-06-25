package com.javafx;

public class Physics {
    /*
     * time in seconds
     * distance in meters
     * angle in degrees
     * velocity in m/s
     * acceleration in m/s^2
     * 
     * the smaller the time used for the calculations the more precise and accurate
     * the results (higher update frequency of velocity)
     */
    private static final double GRAVITATIONAL_ACCELERATION = 9.8067;
    private static final double DRAG_COEFFICIENT_FLIGHT = 0.012;
    private static final double AIR_DENSITY = 1.225;
    private static final double AIR_DENSITY_DROP_RATE = 0.0315; // estimated drop of air density per km
    private static final double WING_AREA = 525.0;
    private static final double MAX_BANK_ANGLE = 66.5;
    private static final double LIFT_COEFFICIENT = 0.52;
    private static final double ACCELERATION = 5;
    private static final int MASS = 396893;
    private static final byte TURN_PER_SECOND = 3;
    private double velocity = 0;
    private double angleDownwards = 0;
    private double angle;
    private boolean crashed = false; // true if plane crashed

    Physics(double angle) {
        this.angle = angle;
    }

    public void sleep(double time, double altitude) {
        velocity -= calcDrag(altitude) * time / MASS;
        double velocityY = (GRAVITATIONAL_ACCELERATION - getLiftForce() / MASS) * time;
        velocity = Math.sqrt(Math.pow(getVelocityX(), 2) + Math.pow(getVelocityZ(), 2) + Math.pow(velocityY, 2));
        angleDownwards = Math.toDegrees(Math.acos(velocityY / velocity));
    }

    private double calcDrag(double altitude) {
        return 0.5 * Math.pow(velocity, 2) * DRAG_COEFFICIENT_FLIGHT * WING_AREA * getAirDensity(altitude);
    }

    public void turn(double angle) {// changes angle
        this.angle += angle;
        // keep the angle between 0 and 360 degrees (for convenience)
        angle += angle < 0 ? 360 : angle > 360 ? (-360) : 0;
    }

    public void turnUp(double angle) {// turns plane up/down
        angleDownwards += angle;
        angleDownwards += angleDownwards < 0 ? 360 : angleDownwards > 360 ? (-360) : 0;
    }

    public double turn(double angle, double time) {// changes angle and simulates resulting rotation, speed changes...
        // returns bank angle of plane
        // only for fast, approximated results
        turn(angle);
        return angle / (360 * time) * 45;
    }

    public double flyCurve(double time, double altitude, byte direction) {// direction -1: left, 1: right
        // returns bank angle of plane
        sleep(time, altitude);
        turn(direction * time * TURN_PER_SECOND);
        double bank_angle = (1.94384 * velocity / 10) + 7;
        crashed = bank_angle > MAX_BANK_ANGLE ? true : false;
        return bank_angle * direction;
    }

    // acceleration and deceleration
    public void accelerate(double time) {
        velocity += ACCELERATION * time;
    }

    public void decelerate(double time) {
        velocity -= ACCELERATION * time;
    }

    // getters for distance
    public double getDeltaX(double time) {
        return time * getVelocityX();
    }

    public double getDeltaZ(double time) {
        return time * getVelocityZ();
    }

    public double getDeltaY(double time) {
        return time * getVelocityY();
    }

    // getter and setter methods

    public boolean getCrashed() {
        return crashed;
    }

    // getters for velocity
    public double getVelocityZ() {
        return Math.sin(Math.toRadians(angleDownwards)) * Math.sin(Math.toRadians(angle)) * velocity;
    }

    public double getVelocityX() {
        return Math.sin(Math.toRadians(angleDownwards)) * Math.cos(Math.toRadians(angle)) * velocity;
    }

    public double getVelocityY() {
        return Math.cos(Math.toRadians(angleDownwards)) * velocity;
    }

    // getters for angle
    public double getAngle() {
        return angle;
    }

    public double getAngleDown() {
        return angleDownwards;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setAngleDown(double angle) {
        this.angleDownwards = angle;
    }

    // private "getters"
    private double getAirDensity(double altitude) {
        return altitude < 10000 ? AIR_DENSITY - (altitude * AIR_DENSITY_DROP_RATE / 1000) : 0.91;
    }

    private double getLiftForce() {
        return 0.5 * AIR_DENSITY * Math.pow(velocity, 2) * WING_AREA * LIFT_COEFFICIENT;
    }
}