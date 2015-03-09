package com.fewlaps.flone.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 Default baud rate: 115200
 Default Pair code: 1234
 */


public class BluetoothCommunication {
    private static final String TAG = "tBlue";

    private String address = null;
    private BluetoothAdapter localAdapter = null;
    private BluetoothDevice remoteDevice = null;
    public BluetoothSocket socket = null;
    public OutputStream outStream = null;
    public InputStream inStream = null;

    public boolean connected = false;

    public BluetoothCommunication(Context context) {
        localAdapter = BluetoothAdapter.getDefaultAdapter();
        if ((localAdapter != null) && localAdapter.isEnabled()) {
            Log.i(TAG, "Bluetooth adapter found and enabled on phone");
        } else {
            Log.e(TAG, "Bluetooth adapter NOT FOUND or NOT ENABLED!");
        }
    }

    public void connect(String address) {
        this.address = address.toUpperCase();
        Log.i(TAG, "Bluetooth connecting to " + address + "...");
        try {
            remoteDevice = localAdapter.getRemoteDevice(address);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Failed to get remote device with MAC address. Wrong format? MAC address must be upper case. ", e);
            return;
        }

        Log.i(TAG, "Creating RFCOMM socket...");
        try {
            Method m = remoteDevice.getClass().getMethod
                    ("createRfcommSocket", new Class[]{
                                    int.class
                            }
                    );
            socket = (BluetoothSocket) m.invoke(remoteDevice, 1);
            Log.i(TAG, "RFCOMM socket created.");
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Could not invoke createRfcommSocket.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Log.i(TAG, "Bad argument with createRfcommSocket.");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.i(TAG, "Illegal access with createRfcommSocket.");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.i(TAG, "Invocation target exception: createRfcommSocket.");
            e.printStackTrace();
        }
        Log.i(TAG, "Got socket for device " + socket.getRemoteDevice());
        localAdapter.cancelDiscovery();

        Log.i(TAG, "Connecting socket...");
        try {
            socket.connect();
            connected = true;
            Log.i(TAG, "Socket connected.");
        } catch (IOException e) {
            try {
                Log.e(TAG, "Failed to connect socket. ", e);
                socket.close();
                Log.e(TAG, "Socket closed because of an error. ", e);
            } catch (IOException eb) {
                Log.e(TAG, "Also failed to close socket. ", eb);
            }
            return;
        }

        try {
            outStream = socket.getOutputStream();
            Log.i(TAG, "Output stream open.");
            inStream = socket.getInputStream();
            Log.i(TAG, "Input stream open.");
        } catch (IOException e) {
            Log.e(TAG, "Failed to create output stream.", e);
        }
        return;
    }

    public void write(String s) {
        //No tags here for performance
        //Log.i(TAG, "Sending \""+s+"\"... ");
        byte[] outBuffer = s.getBytes();
        try {
            outStream.write(outBuffer);
        } catch (IOException e) {
            Log.e(TAG, "Write failed.", e);
        }
    }

    public void write(byte[] outBuffer) {
        //No tags here for performance
        //Log.i(TAG, "Sending ");
        try {
            outStream.write(outBuffer);
        } catch (IOException e) {
            Log.e(TAG, "Write failed.", e);
        }
    }

    public boolean streaming() {
        return ((socket != null) && (inStream != null) && (outStream != null));
    }

    public boolean connected() {
        return (socket != null);
    }

    private byte[] inBuffer = new byte[32];//try?error
    private int bytesRead = 0;

    public void read() {
        try {
            if (inStream.available() > 0) {
                bytesRead = inStream.read(inBuffer);
//                this.floneRemote.parseMSPMessage(inBuffer, bytesRead);
            }
            Log.d(TAG, "Read ok!");
        } catch (Exception e) {
            Log.e(TAG, "Read failed", e);
            inBuffer = null;
        }
    }

    public void close() {
        Log.i(TAG, "Bluetooth closing... ");
        try {
            outStream.close();
            inStream.close();
            socket.close();
            Log.i(TAG, "BT closed");
        } catch (Exception e2) {
            Log.e(TAG, "Failed to close socket. ", e2);
        }
    }
}