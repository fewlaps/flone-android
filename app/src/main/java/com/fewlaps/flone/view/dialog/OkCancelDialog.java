package com.fewlaps.flone.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.fewlaps.flone.R;
import com.fewlaps.flone.view.listener.OnSelectedOkDialogListener;

/**
 * @author Esteve Aguilera (esteve@fewlaps)
 * @date 6/7/15
 */
public class OkCancelDialog extends DialogFragment {

    public static final String TITLE = "title";

    private OnSelectedOkDialogListener listener;

    public static void showDialog(FragmentActivity a, String title) {
        OkCancelDialog frag = new OkCancelDialog();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        frag.setArguments(args);
        frag.show(a.getSupportFragmentManager(), "OkCancelDialog");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(TITLE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(R.string.ok_cancel_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.OnSelectedOkDialogListener();
                            }
                        }
                )
                .setNegativeButton(R.string.ok_cancel_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .create();
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnSelectedOkDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSelectedOkDialogListener");
        }
    }
}


