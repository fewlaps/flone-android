package com.fewlaps.flone.io.bean;

/**
 * A bean with 3 values: heading, pitch and roll.
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 07/05/2015
 */
public class DroneConnectionStatusChanged {
    private boolean connected;

    public DroneConnectionStatusChanged(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
}
