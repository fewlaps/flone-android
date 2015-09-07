package com.fewlaps.flone.io.bean;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 07/05/2015
 */
public class ArmedDataChangeRequest {
    boolean armed;

    public ArmedDataChangeRequest(boolean armed) {
        this.armed = armed;
    }

    public boolean isArmed() {
        return armed;
    }
}
