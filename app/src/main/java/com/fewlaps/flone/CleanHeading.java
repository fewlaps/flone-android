package com.fewlaps.flone;

public class CleanHeading {
    public int getCleanValue(int value) {
        if (value > -179 && value < 180) {
            return value;
        } else if (value < -180) {
            return value + 360;
        } else {
            return value - 360;
        }
    }
}
