package com.fewlaps.flone.data.bean;

import com.fewlaps.flone.data.DefaultValues;

public class PhoneCalibrationData {

    private Double minPitch = DefaultValues.DEFAULT_PHONE_PITCH_ROLL * -1;
    private Double maxPitch = DefaultValues.DEFAULT_PHONE_PITCH_ROLL;
    private Double minRoll = DefaultValues.DEFAULT_PHONE_PITCH_ROLL * -1;
    private Double maxRoll = DefaultValues.DEFAULT_PHONE_PITCH_ROLL;

    private int limit = DefaultValues.DEFAULT_PITCH_ROLL_LIMIT;

    public PhoneCalibrationData() {

    }

    public double getAverageMaxPitch() {
        return ((minPitch * -1) + maxPitch) / 2;
    }

    public double getAverageMaxRoll() {
        return ((minRoll * -1) + maxRoll) / 2;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
