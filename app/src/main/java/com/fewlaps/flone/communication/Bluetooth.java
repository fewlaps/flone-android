package com.fewlaps.flone.communication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Bluetooth extends Communication {

    private static final boolean D = true;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private static final UUID WELL_KNOWN_SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public Bluetooth(Context context) {
        super(context);
    }

    @Override
    public void Enable() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            sendMessageToUI_Toast("Bluetooth is not available on your device");
            // finish();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            return;
        }

        if (D)
            Log.d(TAG, "+++ DONE IN ON CREATE, GOT LOCAL BT ADAPTER +++");

    }

    @Override
    public boolean Connect(String address, int speed) {
        Enable();
        setState(STATE_CONNECTING);
        if (mBluetoothAdapter.isEnabled()) {
            try {
                GetRemoteDevice(address);
                btSocket.connect();
                Connected = true;

                Log.d(TAG, "BT connection established, data transfer link open.");
                setState(STATE_CONNECTED);
            } catch (IOException e) {
                try {
                    btSocket.close();
                    Connected = false;

                    //Toast.makeText(context, context.getString(R.string.Unabletoconnect), Toast.LENGTH_LONG).show();
                } catch (IOException e2) {
                    Log.e(TAG, "ON RESUME: Unable to close socket during connection failure", e2);
                    setState(STATE_NONE);
                }
            }

            // Create a data stream so we can talk to server.
            if (D)
                Log.d(TAG, "+ getOutputStream  getInputStream +");

            try {
                outStream = btSocket.getOutputStream();
                inStream = btSocket.getInputStream();

            } catch (IOException e) {
                Log.e(TAG, "ON RESUME: Output stream creation failed.", e);
                setState(STATE_NONE);
            }
        }
        return Connected;
    }

    @Override
    public boolean dataAvailable() {
        boolean a = false;

        try {
            if (Connected)
                a = inStream.available() > 0;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return a;
    }

    @Override
    public byte Read() {
        BytesReceived += 1;
        byte a = 0;
        try {
            a = (byte) inStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (byte) (a);
    }

    @Override
    public boolean Write(byte[] arr) {
        super.Write(arr);
        try {
            if (Connected)
                outStream.write(arr);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "SEND : Exception during write.", e);
            CloseSocket();

//            Toast.makeText(context, "Write error", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    @Override
    public void Close() {
        CloseSocket();
    }

    @Override
    public void Disable() {
        try {
            mBluetoothAdapter.disable();
        } catch (Exception e) {
            sendMessageToUI_Toast("Can't disable BT");
        }
    }

    @SuppressLint("NewApi")
    private void GetRemoteDevice(String address) {
        if (D) {
            Log.d(TAG, "+ ON RESUME +");
            Log.d(TAG, "+ ABOUT TO ATTEMPT CLIENT CONNECT +");
        }

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {

            btSocket = device.createRfcommSocketToServiceRecord(WELL_KNOWN_SPP_UUID);

        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Socket creation failed.", e);
            sendMessageToUI_Toast("Unable to connect");
        }

        if (mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();
    }

    public void CloseSocket() {
        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "ON PAUSE: Couldn't flush output stream.", e);
                sendMessageToUI_Toast("Unable to close socket");
            }
        }

        try {
            if (btSocket != null)
                btSocket.close();
            Connected = false;
        } catch (Exception e2) {
            Log.e(TAG, "ON PAUSE: Unable to close socket.", e2);
            Toast.makeText(context, "Unable to close socket", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public String getConnectionState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getStrength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public CommunicationMode getMode() {
        return CommunicationMode.BLUETOOTH;
    }

    @Override
    protected void connectionLost() {
        // TODO Auto-generated method stub

    }

}
