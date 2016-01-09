package com.fewlaps.flone;

/**
 * A tool to get the pitch and yaw the user wants to send to the drone.
 * <p>
 * Usually, users won't want to send a 2000 or a 1000, because it's a too high value. So,
 * we'll have to map the input of the user to the bounds he previously set.
 * <p>
 * A limit of 100 makes the min to be 1100 and the max to be 1900.
 */
public class DesiredPitchRollCalculator {

    private int limit = 0;

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getValue(int value) {
        int absolute = getAbsolute(value);

        int calculatedValue = map(absolute, 0, 500, 0, 500 - limit);

        if (value > 1500) {
            return 1500 + calculatedValue;
        } else {
            return 1500 - calculatedValue;
        }
    }

    public int getAbsolute(int value) {
        if (value > 1500) {
            return value - 1500;
        } else {
            int absolute = value - 1500;
            return absolute * -1;
        }
    }

    /**
     * This is the common Arduino map() function, expressed in Java
     * http://stackoverflow.com/questions/7505991/arduino-map-equivalent-function-in-java
     */
    private int map(int x, int inMin, int inMax, int outMin, int outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }
}
