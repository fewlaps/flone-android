package com.fewlaps.flone.view.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fewlaps.flone.R;
import com.fewlaps.flone.io.bean.UsingRawDataChangeRequest;
import com.fewlaps.flone.io.communication.RCSignals;
import com.fewlaps.flone.io.input.phone.RawDataInput;

import de.greenrobot.event.EventBus;

public class SendRawDataDialog extends DialogFragment {

    public static void showDialog(FragmentActivity a) {
        new SendRawDataDialog().show(a.getSupportFragmentManager(), "SendRawDataDialog");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_send_raw_data, null);

        findElement(root, R.id.sb_throttle, R.id.tv_throttle, RCSignals.THROTTLE);
        findElement(root, R.id.sb_roll, R.id.tv_roll, RCSignals.ROLL);
        findElement(root, R.id.sb_pitch, R.id.tv_pitch, RCSignals.PITCH);
        findElement(root, R.id.sb_yaw, R.id.tv_yaw, RCSignals.YAW);
        findElement(root, R.id.sb_aux1, R.id.tv_aux1, RCSignals.AUX1);
        findElement(root, R.id.sb_aux2, R.id.tv_aux2, RCSignals.AUX2);
        findElement(root, R.id.sb_aux3, R.id.tv_aux3, RCSignals.AUX3);
        findElement(root, R.id.sb_aux4, R.id.tv_aux4, RCSignals.AUX4);

        EventBus.getDefault().post(new UsingRawDataChangeRequest(true));
        return root;
    }

    private void findElement(View root, int seekBar, int textView, int signal) {
        SeekBar sb = (SeekBar) root.findViewById(seekBar);
        TextView tv = (TextView) root.findViewById(textView);
        sb.setOnSeekBarChangeListener(new WriteValueOutToTextView(tv, signal));
    }

    class WriteValueOutToTextView implements SeekBar.OnSeekBarChangeListener {

        private TextView tv;
        private int signal;

        public WriteValueOutToTextView(TextView tv, int signal) {
            this.tv = tv;
            this.signal = signal;
            tv.setText(String.valueOf(0));
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                int value = progress + RCSignals.RC_MIN;
                tv.setText(String.valueOf(value));

                if (signal == RCSignals.THROTTLE) {
                    RawDataInput.instance.setThrottle(value);
                } else if (signal == RCSignals.ROLL) {
                    RawDataInput.instance.setRoll(value);
                } else if (signal == RCSignals.PITCH) {
                    RawDataInput.instance.setPitch(value);
                } else if (signal == RCSignals.YAW) {
                    RawDataInput.instance.setHeading(value);
                } else if (signal == RCSignals.AUX1) {
                    RawDataInput.instance.setAux1(value);
                } else if (signal == RCSignals.AUX2) {
                    RawDataInput.instance.setAux2(value);
                } else if (signal == RCSignals.AUX3) {
                    RawDataInput.instance.setAux3(value);
                } else if (signal == RCSignals.AUX4) {
                    RawDataInput.instance.setAux4(value);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        EventBus.getDefault().post(new UsingRawDataChangeRequest(false));
    }
}