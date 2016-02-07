package com.fewlaps.flone.io.bean;

public class UserTouchModeChangedEvent {
    private boolean isTouching = false;

    public UserTouchModeChangedEvent(boolean isTouching) {
        this.isTouching = isTouching;
    }

    public boolean isTouching() {
        return isTouching;
    }
}
