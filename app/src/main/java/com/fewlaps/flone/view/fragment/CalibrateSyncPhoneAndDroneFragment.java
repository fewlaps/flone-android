package com.fewlaps.flone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fewlaps.flone.R;
import com.fewlaps.flone.data.CalibrationDatabase;
import com.fewlaps.flone.data.KnownDronesDatabase;
import com.fewlaps.flone.data.bean.Drone;
import com.fewlaps.flone.data.bean.DroneCalibrationData;
import com.fewlaps.flone.data.bean.PhoneSensorsData;
import com.fewlaps.flone.io.bean.CalibrationDataChangedEvent;
import com.fewlaps.flone.io.bean.DroneSensorData;

import de.greenrobot.event.EventBus;

public class CalibrateSyncPhoneAndDroneFragment extends Fragment {

    public static CalibrateSyncPhoneAndDroneFragment newInstance() {
        return new CalibrateSyncPhoneAndDroneFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calibrate_sync_phone_and_drone, container, false);
        view.findViewById(R.id.bt_sync_sensors).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = CalibrateSyncPhoneAndDroneFragment.this.getActivity();
                Drone drone = KnownDronesDatabase.getSelectedDrone(context);
                DroneCalibrationData data = CalibrationDatabase.getDroneCalibrationData(context, drone.nickName);
                data.setHeadingDifference(phoneHeading - droneHeading);
                CalibrationDatabase.setDroneCalibrationData(context, drone.nickName, data);

                EventBus.getDefault().post(new CalibrationDataChangedEvent());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    double droneHeading;
    double phoneHeading;

    public void onEventMainThread(DroneSensorData data) {
        droneHeading = data.getHeading();
    }

    public void onEventMainThread(PhoneSensorsData data) {
        phoneHeading = data.getHeading();
    }
} 