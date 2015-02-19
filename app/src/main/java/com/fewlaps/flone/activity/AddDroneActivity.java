package com.fewlaps.flone.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fewlaps.flone.R;
import com.fewlaps.flone.adapter.DeviceAdapter;
import com.fewlaps.flone.data.Database;
import com.fewlaps.flone.data.bean.Drone;
import com.fewlaps.flone.dialog.ChooseDeviceNickNameDialog;
import com.fewlaps.flone.listener.OnDeviceNickNameSetListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 19/02/2015
 */
public class AddDroneActivity extends BaseActivity implements OnDeviceNickNameSetListener {

    private ListView listView = null;
    private View zeroCase = null;
    private Drone drone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drone);

        listView = (ListView) findViewById(R.id.lv_devices);
        zeroCase = findViewById(R.id.z_devices);

        findViewById(R.id.bt_launch_bt_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>(getPairedDevices());
        if (devices.isEmpty()) {
            listView.setVisibility(View.INVISIBLE);
            zeroCase.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            zeroCase.setVisibility(View.INVISIBLE);

            listView.setAdapter(new DeviceAdapter(this, devices));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BluetoothDevice device = devices.get(position);
                    drone = new Drone();
                    drone.deviceName = device.getName();
                    drone.address = device.getAddress();
                    ChooseDeviceNickNameDialog.showDialog(AddDroneActivity.this);
                }
            });
        }
    }

    private Set<BluetoothDevice> getPairedDevices() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.getBondedDevices();
    }

    @Override
    public void onDeviceNickNameSetListener(String nickName) {
        drone.nickName = nickName;
        Database.addDrone(this, drone);
        setResult(Activity.RESULT_OK);
        finish();
    }
}
