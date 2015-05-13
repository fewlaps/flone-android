package com.fewlaps.flone.communication.bean;

/**
 * A bean with 3 values: heading, pitch and roll.
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 07/05/2015
 */
public class DroneSensorInformation {
    private double heading;
    private double pitch;
    private double roll;

    public DroneSensorInformation(double heading, double pitch, double roll) {
        this.heading = heading;
        this.pitch = pitch;
        this.roll = roll;
    }

    public double getHeading() {
        return heading;
    }

    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }
}
