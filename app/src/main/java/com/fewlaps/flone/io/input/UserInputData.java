package com.fewlaps.flone.io.input;

import com.fewlaps.flone.io.bean.SensorInformation;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 13/05/2015
 */
public abstract class UserInputData extends SensorInformation {
    private int throttle;

    public int getThrottle() {
        return throttle;
    }

    public void setThrottle(int throttle) {
        this.throttle = throttle;
    }
}
