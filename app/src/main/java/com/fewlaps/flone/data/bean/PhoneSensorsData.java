package com.fewlaps.flone.data.bean;

public class PhoneSensorsData {
    protected double heading = 0;
    protected double pitch = 0;
    protected double roll = 0;

    public PhoneSensorsData(double heading, double pitch, double roll) {
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
