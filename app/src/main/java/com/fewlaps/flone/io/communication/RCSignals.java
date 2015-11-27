package com.fewlaps.flone.io.communication;

import java.util.Arrays;

public class RCSignals {
    public static int RC_MIN = 1000;
    public static int RC_MAX = 2000;
    public static int RC_MID = (RC_MAX - RC_MIN) / 2 + RC_MIN;
    public static int RC_GAP = RC_MAX - RC_MIN;
    public static int RC_MID_GAP = RC_GAP / 2;

    public enum AdjustMode {
        THROTTLE("T: ", (byte) 3), ROLL("R: ", (byte) 0), PITCH("P: ", (byte) 1), YAW("Y: ", (byte) 2);
        String value;
        byte id;

        private AdjustMode(String value, byte id) {
            this.value = value;
            this.id = id;
        }

        public AdjustMode next(AdjustMode t) {
            if (t.ordinal() == AdjustMode.values().length - 1)
                return AdjustMode.values()[0];
            else
                return AdjustMode.values()[t.ordinal() + 1];
        }

        public String getValue() {
            return value;
        }

        public byte getId() {
            return id;
        }
    }

    public static byte ROLL = 0;
    public static byte PITCH = 1;
    public static byte YAW = 2;
    public static byte THROTTLE = 3;
    public static byte AUX1 = 4;
    public static byte AUX2 = 5;
    public static byte AUX3 = 6;
    public static byte AUX4 = 7;
    public AdjustMode adjustMode = AdjustMode.THROTTLE;

    private int[] rc_signals_raw = new int[8];
    private int[] rc_signals_trim = new int[8];
    private int[] rc_signals = new int[8];

    public int ThrottleResolution = 10;
    public int TrimRoll = 0;
    public int TrimPitch = 0;
    public int RollPitchLimit = 500;
    public int ThrottleLimit = 500;
    public int YAW_RESOLUTION = 5;

    public RCSignals() {
        resetRcSignals();
    }

    public void switchMode() {
        adjustMode = AdjustMode.THROTTLE.next(adjustMode);
        if (isFlying() && (adjustMode == AdjustMode.ROLL || adjustMode == AdjustMode.PITCH))
            adjustMode = AdjustMode.YAW;
    }

    public void adjustRcValue(int sign) {
        switch (adjustMode) {
            case THROTTLE:
                set(THROTTLE, get(THROTTLE) + ThrottleResolution * sign);
                break;
            case ROLL:
                trim(ROLL, trim(ROLL) + sign);
                break;
            case PITCH:
                trim(PITCH, trim(PITCH) + sign);
                break;
            case YAW:
                set(YAW, get(YAW) + YAW_RESOLUTION * sign);
                break;
        }
    }

    private void resetRcSignals() {
        rc_signals_raw[ROLL] = RC_MID;
        rc_signals_raw[PITCH] = RC_MID;
        rc_signals_raw[YAW] = RC_MID;
        rc_signals_raw[THROTTLE] = RC_MIN;
        rc_signals_raw[AUX1] = RC_MIN;
        rc_signals_raw[AUX2] = RC_MIN;
        rc_signals_raw[AUX3] = RC_MIN;
        rc_signals_raw[AUX4] = RC_MIN;
    }

    private void updateTrimedValues() {
        for (int x = 0; x < rc_signals.length; x++)
            rc_signals[x] = rc_signals_raw[x];
        rc_signals[ROLL] = rc_signals_raw[ROLL] + TrimRoll;
        rc_signals[PITCH] = rc_signals_raw[PITCH] + TrimPitch;

        for (int x = 0; x < rc_signals.length; x++) { //Verify bounds
            if (rc_signals[x] < RC_MIN) rc_signals[x] = RC_MIN;
            if (rc_signals[x] > RC_MAX) rc_signals[x] = RC_MAX;
        }
    }

    public boolean isFlying() {
        return rc_signals_raw[THROTTLE] > 1100;
    }

    public void setRoll(int roll) {
        rc_signals_raw[ROLL] = roll;
    }

    public void setPitch(int pitch) {
        rc_signals_raw[PITCH] = pitch;
    }

    public void setThrottle(int throttle) {
        rc_signals_raw[THROTTLE] = throttle;
    }

    public void setYaw(int yaw) {
        rc_signals_raw[YAW] = yaw;
    }

    public void setAdjustedRoll(int roll) {
        setRoll((int) (Utilities.map(roll, -500, 500, -RollPitchLimit, RollPitchLimit) + 1500));
    }

    public void setAdjustedPitch(int pitch) {
        setPitch((int) (Utilities.map(pitch, -500, 500, -RollPitchLimit, RollPitchLimit) + 1500));
    }

    public void setAdjustedThrottle(int throttle) {
        setThrottle((int) (Utilities.map(throttle, -500, 500, 0, ThrottleLimit) + 1000));//adjust throttle range....
    }

    public void setAdjustedYaw(int yaw) {
        setYaw((int) (Utilities.map(yaw, -500, 500, -ThrottleLimit, ThrottleLimit) + 1500));
    }

    public void setMin(byte id) {
        rc_signals_raw[id] = RC_MIN;
    }

    public void setMid(byte id) {
        rc_signals_raw[id] = RC_MID;
    }

    public void setMax(byte id) {
        rc_signals_raw[id] = RC_MAX;
    }

    public void setMin(byte[] idArray) {
        for (byte id : idArray)
            rc_signals_raw[id] = RC_MIN;
    }

    public void setMid(byte[] idArray) {
        for (byte id : idArray)
            rc_signals_raw[id] = RC_MID;
    }

    public void setMax(byte[] idArray) {
        for (byte id : idArray)
            rc_signals_raw[id] = RC_MAX;
    }

    public void set(byte id, boolean isMax) {
        if (isMax)
            setMax(id);
        else
            setMin(id);
    }

    public int get(byte id) {
        updateTrimedValues();
        return rc_signals[id];
    }

    public void set(byte id, int value) {
        if (value >= RC_MIN && value <= RC_MAX) {
            rc_signals_raw[id] = value;
        }
    }

    public void trim(byte id, int newValue) {
        rc_signals_trim[id] = newValue;
    }

    public int trim(byte id) {
        return rc_signals_trim[id];
    }

    //TODO
    public int raw_values(byte id) {
        return raw_values()[id];
    }

    //TODO
    public int[] raw_values() {
        return rc_signals_raw;
    }

    public int[] get() {
        updateTrimedValues();
        return rc_signals;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" THROT:" + get(THROTTLE));
        sb.append(" ROLL:" + get(ROLL));
        sb.append(" PITCH:" + get(PITCH));
        sb.append(" YAW:" + get(YAW));
        sb.append(" AUX1:" + get(AUX1));
//        sb.append(" AUX2:" + get(AUX2));
//        sb.append(" AUX3:" + get(AUX3));
//        sb.append(" AUX4:" + get(AUX4));

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RCSignals rcSignals = (RCSignals) o;

        return Arrays.equals(rc_signals_raw, rcSignals.rc_signals_raw);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(rc_signals_raw);
    }
}
