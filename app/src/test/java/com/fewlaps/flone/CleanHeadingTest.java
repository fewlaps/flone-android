package com.fewlaps.flone;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CleanHeadingTest {

    CleanHeading cleanHeading;

    @Before
    public void setup() {
        cleanHeading = new CleanHeading();
    }

    @Test
    public void shouldGiveSameValue_whenSendingMinus170() {
        int value = 170;

        int response = cleanHeading.getCleanValue(value);

        assertEquals(170, response);
    }

    @Test
    public void shouldGiveSameValue_whenSending0() {
        int value = 0;

        int response = cleanHeading.getCleanValue(value);

        assertEquals(0, response);
    }

    @Test
    public void shouldGiveSameValue_whenSending170() {
        int value = 170;

        int response = cleanHeading.getCleanValue(value);

        assertEquals(170, response);
    }

    @Test
    public void shouldGiveMinus170_whenSending190() {
        int value = 190;

        int response = cleanHeading.getCleanValue(value);

        assertEquals(-170, response);
    }

    @Test
    public void shouldGive20_whenSending380() {
        int value = 380;

        int response = cleanHeading.getCleanValue(value);

        assertEquals(20, response);
    }

    @Test
    public void shouldGive0_whenSending360() {
        int value = 360;

        int response = cleanHeading.getCleanValue(value);

        assertEquals(0, response);
    }
}
