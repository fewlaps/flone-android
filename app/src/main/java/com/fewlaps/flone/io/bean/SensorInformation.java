package com.fewlaps.flone.io.bean;

/**
 * A bean with 3 values: heading, pitch and roll.
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 14/05/2015
 */
public abstract class SensorInformation {
    private double heading;
    private double pitch;
    private double roll;

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getRoll() {
        return roll;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    public void update(double heading, double pitch, double roll) {
        this.heading = heading;
        this.pitch = pitch;
        this.roll = roll;
    }
}
