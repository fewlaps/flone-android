package com.fewlaps.flone.communication.bean;

/**
 * A bean with 3 values: heading, pitch and roll.
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 07/05/2015
 */
public class DroneSensorInformation {
    private int yaw;
    private int pitch;
    private int roll;

    public DroneSensorInformation(int yaw, int pitch, int roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public int getYaw() {
        return yaw;
    }

    public int getPitch() {
        return pitch;
    }

    public int getRoll() {
        return roll;
    }

    @Override
    public String toString() {
        return "DroneSensorInformation{" +
                "yaw=" + yaw +
                ", pitch=" + pitch +
                ", roll=" + roll +
                '}';
    }
}
