package com.fewlaps.flone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private TextView droneTV;
    private TextView phoneTV;
    private TextView differenceTV;
    private TextView syncdDifferenceTV;
    private TextView applyedDifferenceTV;

    private Double droneHeading;
    private Double phoneHeading;
    private Double headingDifference;

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
                data.setHeadingDifference(getDifference());
                CalibrationDatabase.setDroneCalibrationData(context, drone.nickName, data);
                updateSyncdDifference();
                EventBus.getDefault().post(new CalibrationDataChangedEvent());
            }
        });

        droneTV = (TextView) view.findViewById(R.id.tv_drone);
        phoneTV = (TextView) view.findViewById(R.id.tv_phone);
        differenceTV = (TextView) view.findViewById(R.id.tv_difference);
        syncdDifferenceTV = (TextView) view.findViewById(R.id.tv_syncd_difference);
        applyedDifferenceTV = (TextView) view.findViewById(R.id.tv_applyed_difference);

        updateSyncdDifference();

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

    public void onEventMainThread(DroneSensorData data) {
        droneHeading = data.getHeading();
        droneTV.setText(getString(R.string.calibration_drone) + " " + data.getHeading());
        updateDifference();
    }

    public void onEventMainThread(PhoneSensorsData data) {
        phoneHeading = data.getHeading();
        phoneTV.setText(getString(R.string.calibration_phone) + " " + data.getHeading());
        updateDifference();
    }

    private void updateDifference() {
        if (droneHeading != null && phoneHeading != null) {
            differenceTV.setText(getString(R.string.calibration_difference) + " " + getDifference());
            applyedDifferenceTV.setText(getString(R.string.calibration_difference_applyed) + " " + getApplyedDifference());
        }
    }

    private void updateSyncdDifference() {
        Drone drone = KnownDronesDatabase.getSelectedDrone(getActivity());
        DroneCalibrationData data = CalibrationDatabase.getDroneCalibrationData(getActivity(), drone.nickName);
        headingDifference = data.getHeadingDifference();

        syncdDifferenceTV.setText(getString(R.string.calibration_difference_syncd) + " " + headingDifference);
    }

    private double getDifference() {
        return phoneHeading - droneHeading;
    }

    private double getApplyedDifference() {
        double difference = phoneHeading - droneHeading - headingDifference;
        if (difference > 180) {
            return difference - 360;
        } else {
            return difference;
        }
    }
} 