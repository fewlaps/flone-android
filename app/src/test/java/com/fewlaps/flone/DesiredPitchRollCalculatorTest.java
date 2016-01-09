package com.fewlaps.flone;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DesiredPitchRollCalculatorTest {

    DesiredPitchRollCalculator calculator;

    @Before
    public void init() {
        calculator = new DesiredPitchRollCalculator();
    }

    @Test
    public void shouldReturn1500For1500Value() {
        assertThat(calculator.getValue(1500)).isEqualTo(1500);
    }

    @Test
    public void shouldReturn1000For1000Value_ifNothingIsSet() {
        assertThat(calculator.getValue(1000)).isEqualTo(1000);
    }

    @Test
    public void shouldReturn1000For1000Value_ifLimitIsSetTo0() {
        calculator.setLimit(0);
        assertThat(calculator.getValue(1000)).isEqualTo(1000);
    }

    @Test
    public void shouldReturn1400For1000Value_ifLimitIsSetTo400() {
        calculator.setLimit(400);
        assertThat(calculator.getValue(1000)).isEqualTo(1400);
    }

    @Test
    public void shouldReturn1001For1000Value_ifLimitIsSetTo1() {
        calculator.setLimit(1);
        assertThat(calculator.getValue(1000)).isEqualTo(1001);
    }

    @Test
    public void shouldReturn1820For1900Value_ifLimitIsSetTo100() {
        calculator.setLimit(100);
        assertThat(calculator.getValue(1900)).isEqualTo(1820);
    }

    @Test
    public void shouldReturn1501For1502Value_ifLimitIsSetTo250() {
        calculator.setLimit(250);
        assertThat(calculator.getValue(1502)).isEqualTo(1501);
    }

    //region getAbsolute
    @Test
    public void getAbsoluteShouldReturn500for2000() {
        assertThat(calculator.getAbsolute(2000)).isEqualTo(500);
    }

    @Test
    public void getAbsoluteShouldReturn500for1000() {
        assertThat(calculator.getAbsolute(1000)).isEqualTo(500);
    }

    @Test
    public void getAbsoluteShouldReturn100for1600() {
        assertThat(calculator.getAbsolute(1600)).isEqualTo(100);
    }
    //endregion
}
