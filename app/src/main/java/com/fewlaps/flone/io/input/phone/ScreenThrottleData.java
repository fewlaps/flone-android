package com.fewlaps.flone.io.input.phone;

import com.fewlaps.flone.io.bean.SensorInformation;
import com.fewlaps.flone.io.communication.RCSignals;

/**
 * A object to map the throttle that the user wants to send using the phone screen
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 13/05/2015
 */
public class ScreenThrottleData extends SensorInformation {
    public static ScreenThrottleData instance = new ScreenThrottleData();

    private Integer screenHeight;

    public Integer getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(Integer screenHeight) {
        this.screenHeight = screenHeight;
    }

    private int throttle = 0;

    public int getThrottle() {
        return throttle;
    }

    public void setThrottle(Integer screenPosition) {
        if (screenPosition == null || screenHeight == null) {
            this.throttle = RCSignals.RC_MIN;
            return;
        }
        int throttlePosition = screenHeight - screenPosition;

        if (throttlePosition > getScreenHeight()) {
            throttlePosition = getScreenHeight();
        } else if (throttlePosition < 0) {
            throttlePosition = 0;
        }

        double throttleRelative = (float) throttlePosition / screenHeight;
        int chosenThrottle = (int) (RCSignals.RC_GAP * throttleRelative);
        this.throttle = RCSignals.RC_MIN + chosenThrottle;
    }

    public float getThrottleScreenPosition() {
        return screenHeight - ((throttle - RCSignals.RC_MIN) * screenHeight / RCSignals.RC_GAP);
    }

    public int getThrottlePorcentage() {
        return (throttle - RCSignals.RC_MIN) * 100 / RCSignals.RC_GAP;
    }
}
