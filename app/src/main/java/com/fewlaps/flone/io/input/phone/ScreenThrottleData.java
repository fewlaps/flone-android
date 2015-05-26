package com.fewlaps.flone.io.input.phone;

import com.fewlaps.flone.io.bean.SensorInformation;

/**
 * A object to map the throttle that the user wants to send using the phone screen
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 13/05/2015
 */
public class ScreenThrottleData extends SensorInformation {
    public static ScreenThrottleData instance = new ScreenThrottleData();

    private Integer screenHeight;

    public static final int MIN_THROTTLE = 1000;
    public static final int MAX_THROTTLE = 2000;
    public static final int GAP_THROTTLE = MAX_THROTTLE - MIN_THROTTLE;

    public Integer getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(Integer screenHeight) {
        this.screenHeight = screenHeight;
    }

    private Integer throttle;

    public Integer getThrottle() {
        return throttle;
    }

    public void setThrottle(Integer screenPosition) {
        int throttlePosition = screenHeight - screenPosition;

        if (throttlePosition > getScreenHeight()) {
            throttlePosition = getScreenHeight();
        } else if (throttlePosition < 0) {
            throttlePosition = 0;
        }

        double throttleRelative = (float) throttlePosition / screenHeight;
        int chosenThrottle = (int) (GAP_THROTTLE * throttleRelative);
        this.throttle = ScreenThrottleData.MIN_THROTTLE + chosenThrottle;
    }
}
