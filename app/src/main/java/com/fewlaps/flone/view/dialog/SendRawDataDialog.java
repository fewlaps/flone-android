package com.fewlaps.flone.view.dialog;

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
import com.fewlaps.flone.io.communication.RCSignals;

public class SendRawDataDialog extends DialogFragment {

    public static void showDialog(FragmentActivity a) {
        new SendRawDataDialog().show(a.getSupportFragmentManager(), "SendRawDataDialog");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_send_raw_data, null);

        SeekBar throttleSB = (SeekBar) root.findViewById(R.id.sb_throttle);
        TextView throttleTV = (TextView) root.findViewById(R.id.tv_throttle);
        throttleSB.setOnSeekBarChangeListener(new WriteValueOutToTextView(throttleTV));

        SeekBar rollSB = (SeekBar) root.findViewById(R.id.sb_roll);
        TextView rollTV = (TextView) root.findViewById(R.id.tv_roll);
        rollSB.setOnSeekBarChangeListener(new WriteValueOutToTextView(rollTV));

        SeekBar pitchSB = (SeekBar) root.findViewById(R.id.sb_pitch);
        TextView pitchTV = (TextView) root.findViewById(R.id.tv_pitch);
        pitchSB.setOnSeekBarChangeListener(new WriteValueOutToTextView(pitchTV));

        SeekBar yawSB = (SeekBar) root.findViewById(R.id.sb_yaw);
        TextView yawTV = (TextView) root.findViewById(R.id.tv_yaw);
        yawSB.setOnSeekBarChangeListener(new WriteValueOutToTextView(yawTV));

        SeekBar aux1SB = (SeekBar) root.findViewById(R.id.sb_aux1);
        TextView aux1TV = (TextView) root.findViewById(R.id.tv_aux1);
        aux1SB.setOnSeekBarChangeListener(new WriteValueOutToTextView(aux1TV));

        SeekBar aux2SB = (SeekBar) root.findViewById(R.id.sb_aux2);
        TextView aux2TV = (TextView) root.findViewById(R.id.tv_aux2);
        aux2SB.setOnSeekBarChangeListener(new WriteValueOutToTextView(aux2TV));

        SeekBar aux3SB = (SeekBar) root.findViewById(R.id.sb_aux3);
        TextView aux3TV = (TextView) root.findViewById(R.id.tv_aux3);
        aux3SB.setOnSeekBarChangeListener(new WriteValueOutToTextView(aux3TV));

        SeekBar aux4SB = (SeekBar) root.findViewById(R.id.sb_aux4);
        TextView aux4TV = (TextView) root.findViewById(R.id.tv_aux4);
        aux4SB.setOnSeekBarChangeListener(new WriteValueOutToTextView(aux4TV));

        return root;
    }

    class WriteValueOutToTextView implements SeekBar.OnSeekBarChangeListener {

        TextView tv;

        public WriteValueOutToTextView(TextView tv) {
            this.tv = tv;
            tv.setText(String.valueOf(0));
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                tv.setText(String.valueOf(progress + RCSignals.RC_MIN));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
} 