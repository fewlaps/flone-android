package com.fewlaps.flone.data.bean;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 26/05/2015
 */
public class PhoneCalibrationData {
    public Double minPitch;
    public Double maxPitch;
    public Double minRoll;
    public Double maxRoll;

    public PhoneCalibrationData() {
        minPitch = -50d;
        maxPitch = 50d;
        minRoll = -50d;
        maxRoll = 50d;
    }

    public double getAverageMaxPitch() {
        return ((minPitch * -1) + maxPitch) / 2;
    }

    public double getAverageMaxRoll() {
        return ((minRoll * -1) + maxRoll) / 2;
    }
}
