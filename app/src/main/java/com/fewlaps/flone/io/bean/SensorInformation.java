package com.fewlaps.flone.io.bean;

/**
 * A bean with 3 values: heading, pitch and roll.
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 14/05/2015
 */
public abstract class SensorInformation {
    private int heading;
    private int pitch;
    private int roll;

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public void update(int heading, int pitch, int roll) {
        this.heading = heading;
        this.pitch = pitch;
        this.roll = roll;
    }
}
