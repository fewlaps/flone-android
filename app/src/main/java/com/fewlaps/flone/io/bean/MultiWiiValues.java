package com.fewlaps.flone.io.bean;

import com.fewlaps.flone.io.communication.RCSignals;

public class MultiWiiValues {
    protected double throttle = 0;
    protected double heading = 0;
    protected double pitch = 0;
    protected double roll = 0;
    protected double aux1 = 0;
    protected double aux2 = 0;
    protected double aux3 = 0;
    protected double aux4 = 0;

    public double getThrottle() {
        return throttle;
    }

    public void setThrottle(double throttle) {
        this.throttle = throttle;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getRoll() {
        return roll;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    public double getAux1() {
        return aux1;
    }

    public void setAux1(double aux1) {
        this.aux1 = aux1;
    }

    public double getAux2() {
        return aux2;
    }

    public void setAux2(double aux2) {
        this.aux2 = aux2;
    }

    public double getAux3() {
        return aux3;
    }

    public void setAux3(double aux3) {
        this.aux3 = aux3;
    }

    public double getAux4() {
        return aux4;
    }

    public void setAux4(double aux4) {
        this.aux4 = aux4;
    }

    public boolean isDifferentThanRcValues(RCSignals rc) {
        if (rc.get(RCSignals.THROTTLE) != throttle) {
            return true;
        }
        if (rc.get(RCSignals.ROLL) != roll) {
            return true;
        }
        if (rc.get(RCSignals.PITCH) != pitch) {
            return true;
        }
        if (rc.get(RCSignals.YAW) != heading) {
            return true;
        }
        if (rc.get(RCSignals.AUX1) != aux1) {
            return true;
        }
        if (rc.get(RCSignals.AUX2) != aux2) {
            return true;
        }
        if (rc.get(RCSignals.AUX3) != aux3) {
            return true;
        }
        if (rc.get(RCSignals.AUX4) != aux4) {
            return true;
        }

        return false;
    }

    public void update(double heading, double pitch, double roll) {
        this.heading = heading;
        this.pitch = pitch;
        this.roll = roll;
    }

    public void update(RCSignals rc) {
        update(rc.get(RCSignals.THROTTLE),
                rc.get(RCSignals.YAW),
                rc.get(RCSignals.PITCH),
                rc.get(RCSignals.ROLL),
                rc.get(RCSignals.AUX1),
                rc.get(RCSignals.AUX2),
                rc.get(RCSignals.AUX3),
                rc.get(RCSignals.AUX4)
        );
    }

    public void update(double throttle, double heading, double pitch, double roll, double aux1, double aux2, double aux3, double aux4) {
        this.throttle = throttle;
        this.heading = heading;
        this.pitch = pitch;
        this.roll = roll;
        this.aux1 = aux1;
        this.aux2 = aux2;
        this.aux3 = aux3;
        this.aux4 = aux4;
    }
}
