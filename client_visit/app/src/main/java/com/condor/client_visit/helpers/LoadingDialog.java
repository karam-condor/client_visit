package com.condor.client_visit.helpers;

import android.app.AlertDialog;
import android.content.Context;

import com.condor.client_visit.R;

public class LoadingDialog {
    public static AlertDialog loadingDialog;

    public static void showLoadingDialog(Context context){
        loadingDialog = new AlertDialog.Builder(context)
                .setView(R.layout.loading_bar_layout)
                .setCancelable(false)
                .create();
        loadingDialog.show();
    }

    public static void closeLoadingDialog(){
        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }
}
