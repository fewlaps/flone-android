package com.fewlaps.flone.communication.bean;

/**
 * A bean with 3 values: heading, pitch and roll.
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 13/05/2015
 */
public class PhoneSensorInformation {
    private double yaw;
    private double pitch;
    private double roll;

    public PhoneSensorInformation() {

    }

    public PhoneSensorInformation(double yaw, double pitch, double roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
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

    @Override
    public String toString() {
        return "PhoneSensorInformation{" +
                "yaw=" + yaw +
                ", pitch=" + pitch +
                ", roll=" + roll +
                '}';
    }
}
