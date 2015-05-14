package com.fewlaps.flone.io.communication;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

public class Wifi extends Communication {
	private Socket mySocket;
	private WifiManager wifiManager;

	public Wifi(Context context) {
		super(context);
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

	}

	private boolean isAlreadyConnectedToSSID(String ssid) {
	String mSSID = getCurrentSSID();
	if(mSSID == null) return false;
		return mSSID.equalsIgnoreCase(ssid);
	}
	public String getCurrentSSID() {
	  String ssid = null;
	  ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	  NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	  if (networkInfo.isConnected()) {
		final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
		  ssid = connectionInfo.getSSID();
		}
	  }
	  return ssid;
	}
	private WifiConfiguration getConfiguredNetwork(WifiManager wifiManager,	String ssid) {
		List<WifiConfiguration> myList = wifiManager.getConfiguredNetworks();
		for (WifiConfiguration current : myList) {
			String wifiConfigSSID = current.SSID.replace("\"", "");
			if (wifiConfigSSID.equalsIgnoreCase(ssid))
				return current;
		}
		return null;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getConnectionState() {
		return state;
	}

	public boolean Write(byte[] data) {
		super.Write(data);
		try {
			outStream.write(data);
			Connected = true;
		} catch (IOException e) {
			e.printStackTrace();
			Connected = false;
		}
		return Connected;
	}

	public boolean flush() {
		try {
			outStream.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void Enable() {
		if (!wifiManager.isWifiEnabled())
			wifiManager.setWifiEnabled(true);
	}

	@Override
	public boolean Connect(String ip, int port) {
		Enable();
		address = ip + ":" + port;
		setState(STATE_CONNECTING);
		try {
			mySocket = new Socket(ip, port);
			mySocket.setKeepAlive(true);
			inStream = mySocket.getInputStream();
			outStream = mySocket.getOutputStream();
			setState(STATE_CONNECTED);
			//Connected = true;
		} catch (Exception e) {
			e.printStackTrace();
			connectionLost();
			return false;
		}
		//Log.d("connect", "connected!!!!!");
		return true;
	}

	@Override
	protected void connectionLost() {
		setState(STATE_NONE);
		//setState(e.getMessage());
		Connected = false;
	}
	
	@Override
	public boolean dataAvailable() {
		try {
			if (inStream == null)
				return false;
			return inStream.available() != 0;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
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
	public void Close() {
		Connected = false;
		if (mySocket != null && mySocket.isConnected())
			try {
				mySocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void Disable() {
		if (wifiManager.isWifiEnabled())
			wifiManager.setWifiEnabled(false);
	}

	@Override
	public int getStrength() {
	      try {
	         int rssi = wifiManager.getConnectionInfo().getRssi();
	         int level = WifiManager.calculateSignalLevel(rssi, 10);
	         int percentage = (int) ((level / 10.0) * 100);
	         Log.d("aaa", "WiFI RSSI=" + String.valueOf(percentage));
	         return percentage;

	      } catch (Exception e) {
	         return 0;
	      }
	   }
	@Override
	public CommunicationMode getMode() 
	{
	return CommunicationMode.WIFI;
	}
}
