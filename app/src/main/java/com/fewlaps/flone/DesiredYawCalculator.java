package com.fewlaps.flone;

/**
 * A tool to get the yaw the user wants to send to the drone. The idea is that
 * droneHeading and desiresHeading are values from -180 to 180.
 */
public class DesiredYawCalculator {
    public double getYaw(double droneHeading, double phoneHeading) {
        double difference = phoneHeading - droneHeading;

        if (difference > 180) {
            difference = difference - 180 * 2;
        } else if (difference < -180) {
            difference = difference + 180 * 2;
        }

        return difference;
    }
}
