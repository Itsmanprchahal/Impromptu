package com.mandywebdesign.impromptu.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.mandywebdesign.impromptu.R;

public class ProgressBarClass {

    public static Dialog showProgressDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.custom_progress);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }
}
