package com.condor.client_visit.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.condor.client_visit.R;

public class SimpleMessageDialog {

    public static AlertDialog msgDialog;

    public static void showMessage(Context context, String title, String body){
        msgDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(body)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        msgDialog.dismiss();
                    }
                })
                .create();
        msgDialog.show();
    }
}
