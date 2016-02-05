package com.fewlaps.flone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.fewlaps.flone.R;
import com.fewlaps.flone.io.bean.CalibrateDroneAccelerometerRequest;
import com.fewlaps.flone.io.bean.CalibrateDroneMagnetometerRequest;
import com.fewlaps.flone.io.bean.DroneSensorData;

import de.greenrobot.event.EventBus;

public class CalibrateDroneSensorsFragment extends Fragment {

    public static CalibrateDroneSensorsFragment newInstance() {
        return new CalibrateDroneSensorsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private SeekBar rollSB;
    private SeekBar pitchSB;
    private SeekBar headingSB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calibrate_drone_sensors, container, false);

        rollSB = (SeekBar) view.findViewById(R.id.sb_roll);
        pitchSB = (SeekBar) view.findViewById(R.id.sb_pitch);
        headingSB = (SeekBar) view.findViewById(R.id.sb_heading);

        view.findViewById(R.id.bt_start_accelerometer_calibration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new CalibrateDroneAccelerometerRequest());
                Toast.makeText(CalibrateDroneSensorsFragment.this.getActivity(), getString(R.string.calibration_accelerometer_wait_ten_seconds), Toast.LENGTH_LONG).show();
            }
        });

        view.findViewById(R.id.bt_start_magnetometer_calibration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new CalibrateDroneMagnetometerRequest());
                Toast.makeText(CalibrateDroneSensorsFragment.this.getActivity(), getString(R.string.calibration_magnetometer_move_all_axis), Toast.LENGTH_LONG).show();
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

    public void onEventMainThread(DroneSensorData data) {
        updateScreen(data.getPitch(), data.getRoll(), data.getHeading());
    }

    private void updateScreen(double pitch, double roll, double heading) {
        rollSB.setProgress((int) (roll + rollSB.getMax() / 2));
        pitchSB.setProgress((int) (pitch + pitchSB.getMax() / 2));
        headingSB.setProgress((int) (heading + headingSB.getMax() / 2));
    }
} 