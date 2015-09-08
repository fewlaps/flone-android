package com.fewlaps.flone.io.input;

/**
 * The interface that represents what the user wants to do with the Flone.
 * Possible implementations of this interface would be: an Input that works with the
 * sensors, the buttons and the screen of the phone, or an Input that gets the
 * data from the Internet
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 14/05/2015
 */
public interface UserInstructionsInput {
    public double getThrottle();

    public double getHeading();

    public double getPitch();

    public double getRoll();
}
