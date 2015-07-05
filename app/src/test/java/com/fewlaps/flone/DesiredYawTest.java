package com.fewlaps.flone;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 05/07/2015
 */
public class DesiredYawTest {

    DesiredYawCalculator desiredYaw;

    @Before
    public void init(){
        desiredYaw = new DesiredYawCalculator();
    }

    @Test
    public void shouldReturn0ForSameValues() {
        double sameValue = 42;
        double yaw = desiredYaw.getYaw(sameValue, sameValue);

        assertEquals(0d, yaw);
    }

    @Test
     public void shouldReturn20ForDroneAt0AndPhoneAt20() {
        double yaw = desiredYaw.getYaw(0, 20);

        assertEquals(20d, yaw);
    }

    @Test
    public void shouldReturn30ForDroneAt50AndPhoneAt80() {
        double yaw = desiredYaw.getYaw(50, 80);

        assertEquals(30d, yaw);
    }

    @Test
    public void shouldReturn20ForDroneAtMinus10AndPhoneAt10() {
        double yaw = desiredYaw.getYaw(-10, 10);

        assertEquals(20d, yaw);
    }

    @Test
    public void shouldReturn20ForDroneAtMinus170AndPhoneAtMinus150() {
        double yaw = desiredYaw.getYaw(-170, -150);

        assertEquals(20d, yaw);
    }

    @Test
    public void shouldReturnMinus20ForDroneAtMinus170AndPhoneAt170() {
        double yaw = desiredYaw.getYaw(-170, 170);

        assertEquals(-20d, yaw);
    }

    @Test
    public void shouldReturn20ForDroneAt170AndPhoneAtMinus170() {
        double yaw = desiredYaw.getYaw(170, -170);

        assertEquals(20d, yaw);
    }

    @Test
    public void shouldReturn170ForDroneAtMinus20AndPhoneAt150() {
        double yaw = desiredYaw.getYaw(-20, 150);

        assertEquals(170d, yaw);
    }
}
