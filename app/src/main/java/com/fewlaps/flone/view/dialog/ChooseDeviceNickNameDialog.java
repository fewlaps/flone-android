package com.fewlaps.flone.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.fewlaps.flone.R;
import com.fewlaps.flone.view.listener.OnDeviceNickNameSetListener;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 19/02/2015
 */
public class ChooseDeviceNickNameDialog extends DialogFragment {

    public static void showDialog(FragmentActivity a) {
        new ChooseDeviceNickNameDialog().show(a.getSupportFragmentManager(), "ChooseDeviceNickNameDialog");
    }

    private OnDeviceNickNameSetListener listener = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View root = inflater.inflate(R.layout.dialog_choose_nick, null);
        builder.setView(root).setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText nickNameET = (EditText) root.findViewById(R.id.et_nickname);
                listener.onDeviceNickNameSetListener(nickNameET.getText().toString());
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnDeviceNickNameSetListener) activity;
    }
} 