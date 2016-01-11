package com.fewlaps.flone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.fewlaps.flone.DesiredPitchRollCalculator;
import com.fewlaps.flone.R;
import com.fewlaps.flone.data.CalibrationDatabase;
import com.fewlaps.flone.data.DefaultValues;
import com.fewlaps.flone.data.bean.PhoneCalibrationData;
import com.fewlaps.flone.io.bean.CalibrationDataChangedEvent;

import de.greenrobot.event.EventBus;

public class PreferenceCapPitchRollFragment extends Fragment {

    private Context context;

    public static PreferenceCapPitchRollFragment newInstance() {
        return new PreferenceCapPitchRollFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preferences_cap_values, container, false);
        context = PreferenceCapPitchRollFragment.this.getContext();

        final SeekBar seekBar = (SeekBar) view.findViewById(R.id.sb_value);
        seekBar.setProgress(DesiredPitchRollCalculator.MAX_LIMIT - CalibrationDatabase.getPhoneCalibrationData(context).getLimit());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                PhoneCalibrationData data = CalibrationDatabase.getPhoneCalibrationData(context);
                data.setLimit(DesiredPitchRollCalculator.MAX_LIMIT - progress);
                CalibrationDatabase.setPhoneCalibrationData(context, data);

                EventBus.getDefault().post(new CalibrationDataChangedEvent());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        view.findViewById(R.id.bt_load_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setProgress(DefaultValues.DEFAULT_PITCH_ROLL_LIMIT);
            }
        });

        return view;
    }
} 