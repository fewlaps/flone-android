package com.fewlaps.flone.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @see <a href="https://gist.github.com/rocboronat/f09b14cdceee8b3a4d3e">The original gist</a>
 */
public class BluetoothUtil {

    public static void startBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
        }
    }

    public static void stopBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
    }

    public static void unpairMac(String macToRemove) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
            try {
                Class<?> btDeviceInstance = Class.forName(BluetoothDevice.class.getCanonicalName());
                Method removeBondMethod = btDeviceInstance.getMethod("removeBond");
                boolean cleared = false;
                for (BluetoothDevice bluetoothDevice : bondedDevices) {
                    String mac = bluetoothDevice.getAddress();
                    if (mac.equals(macToRemove)) {
                        removeBondMethod.invoke(bluetoothDevice);
                        Log.i("BT", "Cleared Pairing");
                        cleared = true;
                        break;
                    }
                }

                if (!cleared) {
                    Log.i("BT", "Not Paired");
                }
            } catch (Throwable th) {
                Log.e("BT", "Error pairing", th);
            }
        }
    }
}