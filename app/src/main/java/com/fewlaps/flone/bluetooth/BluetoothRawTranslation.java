package com.fewlaps.flone.bluetooth;

import android.util.Log;

/**
 * Created by Roc Boronat (roc@fewlaps.com) on 09/03/2015.
 */
public class BluetoothRawTranslation {
    public static final String LOG = "MULTIWII";

    static int c;
    static int k;
    static int p;
    static int i, j;

    static int multiType;
    static boolean lock = true;
    private static int present = 0;
    static int time, time2, time3, time4;

    private static final String MSP_HEADER = "$M<";
    private static final byte[] MSP_HEADER_BYTE = MSP_HEADER.getBytes();
    private static final int headerLength = MSP_HEADER_BYTE.length;

    static final int byteMP[] = new int[8];  // Motor Pins.  Varies by multiType and Arduino model (pro Mini, Mega, etc).

    private static final int
            MSP_IDENT = 100,
            MSP_STATUS = 101,
            MSP_RAW_IMU = 102,
            MSP_SERVO = 103,
            MSP_MOTOR = 104,
            MSP_RC = 105,
            MSP_RAW_GPS = 106,
            MSP_COMP_GPS = 107,
            MSP_ATTITUDE = 108,
            MSP_ALTITUDE = 109,
            MSP_BAT = 110,
            MSP_RC_TUNING = 111,
            MSP_PID = 112,
            MSP_BOX = 113,
            MSP_MISC = 114,
            MSP_MOTOR_PINS = 115,
            MSP_BOXNAMES = 116,
            MSP_PIDNAMES = 117,

    MSP_SET_RAW_RC = 200,
            MSP_SET_RAW_GPS = 201,
            MSP_SET_PID = 202,
            MSP_SET_BOX = 203,
            MSP_SET_RC_TUNING = 204,
            MSP_ACC_CALIBRATION = 205,
            MSP_MAG_CALIBRATION = 206,
            MSP_SET_MISC = 207,
            MSP_RESET_CONF = 208,
            MSP_SELECT_SETTING = 210,

    MSP_BIND = 240,

    MSP_EEPROM_WRITE = 250,

    MSP_DEBUGMSG = 253,
            MSP_DEBUG = 254;

    public static final int
            IDLE = 0,
            HEADER_START = 1,
            HEADER_M = 2,
            HEADER_ARROW = 3,
            HEADER_SIZE = 4,
            HEADER_CMD = 5,
            HEADER_ERR = 6;

    static int c_state = IDLE;
    static boolean err_rcvd = false;

    static byte checksum = 0;
    static byte cmd;
    static int offset = 0, dataSize = 0;
    static byte[] inBuf = new byte[256];

    static float mot[] = new float[8];
    static final int minRC = 1000, maxRC = 2000, medRC = 1500;

    static int version, versionMisMatch;
    static float gx, gy, gz, ax, ay, az, magx, magy, magz, alt, head, angx, angy, debug1, debug2, debug3, debug4;
    static int GPS_distanceToHome, GPS_directionToHome,
            GPS_numSat, GPS_fix, GPS_update, GPS_altitude, GPS_speed,
            GPS_latitude, GPS_longitude,
            init_com, graph_on, pMeterSum, intPowerTrigger, bytevbat;

    static int servo[] = new int[8],
            rcThrottle = minRC, rcRoll = medRC, rcPitch = medRC, rcYaw = medRC,
            rcAUX1 = minRC, rcAUX2 = minRC, rcAUX3 = minRC, rcAUX4 = minRC;

    static int cycleTime, i2cError;

    public static int read32() {
        return (inBuf[p++] & 0xff) + ((inBuf[p++] & 0xff) << 8) + ((inBuf[p++] & 0xff) << 16) + ((inBuf[p++] & 0xff) << 24);
    }

    public static int read16() {
        return (inBuf[p++] & 0xff) + ((inBuf[p++]) << 8);
    }

    public static int read8() {
        return inBuf[p++] & 0xff;
    }

    static int mode;
    static int multiCapability;

    public static void parseMSPMessage(byte[] data, int dataLength) {
        for (k = 0; k < dataLength; k++) {
            c = data[k];

            if (c_state == IDLE) {
                c_state = (c == '$') ? HEADER_START : IDLE;
            } else if (c_state == HEADER_START) {
                c_state = (c == 'M') ? HEADER_M : IDLE;
            } else if (c_state == HEADER_M) {
                if (c == '>') {
                    c_state = HEADER_ARROW;
                } else if (c == '!') {
                    c_state = HEADER_ERR;
                } else {
                    c_state = IDLE;
                }
            } else if (c_state == HEADER_ARROW || c_state == HEADER_ERR) {
                // is this an error message?
                err_rcvd = (c_state == HEADER_ERR);        // now we are expecting the payload size
                dataSize = (c & 0xFF);
                // reset index variables
                p = 0;
                offset = 0;
                checksum = 0;
                checksum ^= (c & 0xFF);
                // the command is to follow
                c_state = HEADER_SIZE;
            } else if (c_state == HEADER_SIZE) {
                cmd = (byte) (c & 0xFF);
                checksum ^= (c & 0xFF);
                c_state = HEADER_CMD;
            } else if (c_state == HEADER_CMD && offset < dataSize) {
                checksum ^= (c & 0xFF);
                inBuf[offset++] = (byte) (c & 0xFF);
            } else if (c_state == HEADER_CMD && offset >= dataSize) {
                // compare calculated and transferred checksum
                if ((checksum & 0xFF) == (c & 0xFF)) {
                    if (err_rcvd) {
                        System.err.println("Copter did not understand request type " + c);
                    } else {
                        // we got a valid response packet, evaluate it
                        evaluateCommand(cmd, (int) dataSize);
                    }
                } else {
                    System.out.println("invalid checksum for command " + ((int) (cmd & 0xFF)) + ": " + (checksum & 0xFF) + " expected, got " + (int) (c & 0xFF));
                    System.out.print("<" + (cmd & 0xFF) + " " + (dataSize & 0xFF) + "> {");
                    for (i = 0; i < dataSize; i++) {
                        if (i != 0) {
                            System.err.print(' ');
                        }
                        System.out.print((inBuf[i] & 0xFF));
                    }
                    System.out.println("} [" + c + "]");
                    System.out.println(new String(inBuf, 0, dataSize));
                }
                c_state = IDLE;
            }
        }
    }

    public static void evaluateCommand(byte cmd, int dataSize) {
        int icmd = (int) (cmd & 0xFF);
        switch (icmd) {
            case MSP_IDENT:
                version = read8();
                multiType = read8();
                read8(); // MSP version
                multiCapability = read32();// capability
                Log.i(LOG, "IDent:" + version + " " + multiType + " " + multiCapability);
                break;
            case MSP_STATUS:
                cycleTime = read16();
                i2cError = read16();
                present = read16();
                mode = read32();
    /*if ((present&1) >0) {buttonAcc.setColorBackground(green_);} else {buttonAcc.setColorBackground(red_);tACC_ROLL.setState(false); tACC_PITCH.setState(false); tACC_Z.setState(false);}
     if ((present&2) >0) {buttonBaro.setColorBackground(green_);} else {buttonBaro.setColorBackground(red_); tBARO.setState(false); }
     if ((present&4) >0) {buttonMag.setColorBackground(green_);} else {buttonMag.setColorBackground(red_); tMAGX.setState(false); tMAGY.setState(false); tMAGZ.setState(false); }
     if ((present&8) >0) {buttonGPS.setColorBackground(green_);} else {buttonGPS.setColorBackground(red_); tHEAD.setState(false);}
     if ((present&16)>0) {buttonSonar.setColorBackground(green_);} else {buttonSonar.setColorBackground(red_);}
     for(i=0;i<CHECKBOXITEMS;i++) {
     if ((mode&(1<<i))>0) buttonCheckbox[i].setColorBackground(green_); else buttonCheckbox[i].setColorBackground(red_);
     }*/
                read8();
                //confSetting.setValue(read8());
                //confSetting.setColorBackground(green_);
                break;
            case MSP_RAW_IMU:
                ax = read16();
                ay = read16();
                az = read16();
                gx = read16() / 8;
                gy = read16() / 8;
                gz = read16() / 8;
                magx = read16() / 3;
                magy = read16() / 3;
                magz = read16() / 3;
                break;
            case MSP_SERVO:
                for (i = 0; i < 8; i++) servo[i] = read16();
                break;
            case MSP_MOTOR:
                for (i = 0; i < 8; i++) mot[i] = read16();
                break;
            case MSP_RC:
                rcRoll = read16();
                rcPitch = read16();
                rcYaw = read16();
                rcThrottle = read16();
                rcAUX1 = read16();
                rcAUX2 = read16();
                rcAUX3 = read16();
                rcAUX4 = read16();
                break;
            case MSP_RAW_GPS:
                GPS_fix = read8();
                GPS_numSat = read8();
                GPS_latitude = read32();
                GPS_longitude = read32();
                GPS_altitude = read16();
                GPS_speed = read16();
                break;
            case MSP_COMP_GPS:
                GPS_distanceToHome = read16();
                GPS_directionToHome = read16();
                GPS_update = read8();
                break;
            case MSP_ATTITUDE:
                angx = read16() / 10;
                angy = read16() / 10;
                head = read16();
                break;
            case MSP_ALTITUDE:
                alt = read32();
                break;
            case MSP_BAT:
                bytevbat = read8();
                pMeterSum = read16();

                break;
    /*case MSP_RC_TUNING:
     byteRC_RATE = read8();byteRC_EXPO = read8();byteRollPitchRate = read8();
     byteYawRate = read8();byteDynThrPID = read8();
     byteThrottle_MID = read8();byteThrottle_EXPO = read8();
     confRC_RATE.setValue(byteRC_RATE/100.0);
     confRC_EXPO.setValue(byteRC_EXPO/100.0);
     rollPitchRate.setValue(byteRollPitchRate/100.0);
     yawRate.setValue(byteYawRate/100.0);
     dynamic_THR_PID.setValue(byteDynThrPID/100.0);
     throttle_MID.setValue(byteThrottle_MID/100.0);
     throttle_EXPO.setValue(byteThrottle_EXPO/100.0);
     confRC_RATE.setColorBackground(green_);confRC_EXPO.setColorBackground(green_);rollPitchRate.setColorBackground(green_);
     yawRate.setColorBackground(green_);dynamic_THR_PID.setColorBackground(green_);
     throttle_MID.setColorBackground(green_);throttle_EXPO.setColorBackground(green_);
     updateModelMSP_SET_RC_TUNING();
     break;*/
            case MSP_ACC_CALIBRATION:
                break;
            case MSP_MAG_CALIBRATION:
                break;
    /*case MSP_PID:
     for(i=0;i<PIDITEMS;i++) {
     byteP[i] = read8();byteI[i] = read8();byteD[i] = read8();
     switch (i) {
     case 0:
     confP[i].setValue(byteP[i]/10.0);confI[i].setValue(byteI[i]/1000.0);confD[i].setValue(byteD[i]);
     break;
     case 1:
     confP[i].setValue(byteP[i]/10.0);confI[i].setValue(byteI[i]/1000.0);confD[i].setValue(byteD[i]);
     break;
     case 2:
     confP[i].setValue(byteP[i]/10.0);confI[i].setValue(byteI[i]/1000.0);confD[i].setValue(byteD[i]);
     break;
     case 3:
     confP[i].setValue(byteP[i]/10.0);confI[i].setValue(byteI[i]/1000.0);confD[i].setValue(byteD[i]);
     break;
     case 7:
     confP[i].setValue(byteP[i]/10.0);confI[i].setValue(byteI[i]/1000.0);confD[i].setValue(byteD[i]);
     break;
     case 8:
     confP[i].setValue(byteP[i]/10.0);confI[i].setValue(byteI[i]/1000.0);confD[i].setValue(byteD[i]);
     break;
     case 9:
     confP[i].setValue(byteP[i]/10.0);confI[i].setValue(byteI[i]/1000.0);confD[i].setValue(byteD[i]);
     break;
     //Different rates fot POS-4 POSR-5 NAVR-6
     case 4:
     confP[i].setValue(byteP[i]/100.0);confI[i].setValue(byteI[i]/100.0);confD[i].setValue(byteD[i]/1000.0);
     break;
     case 5:
     confP[i].setValue(byteP[i]/10.0);confI[i].setValue(byteI[i]/100.0);confD[i].setValue(byteD[i]/1000.0);
     break;
     case 6:
     confP[i].setValue(byteP[i]/10.0);confI[i].setValue(byteI[i]/100.0);confD[i].setValue(byteD[i]/1000.0);
     break;
     }
     confP[i].setColorBackground(green_);
     confI[i].setColorBackground(green_);
     confD[i].setColorBackground(green_);
     }
     updateModelMSP_SET_PID();
     break;*/

    /*case MSP_MISC:
     intPowerTrigger = read16();
     confPowerTrigger.setValue(intPowerTrigger);
     updateModelMSP_SET_MISC();
     break;*/
            case MSP_MOTOR_PINS:
                for (i = 0; i < 8; i++) {
                    byteMP[i] = read8();
                }
                break;
            case MSP_DEBUGMSG:
                while (dataSize-- > 0) {
                    char c = (char) read8();
                    if (c != 0) {
                        System.out.print(c);
                    }
                }
                break;
            case MSP_DEBUG:
                debug1 = read16();
                debug2 = read16();
                debug3 = read16();
                debug4 = read16();
                break;
            case MSP_SET_RAW_RC:
                //Measure time
                break;
            default:
                Log.i(LOG, "Don't know how to handle reply " + icmd);
        }
    }
}
