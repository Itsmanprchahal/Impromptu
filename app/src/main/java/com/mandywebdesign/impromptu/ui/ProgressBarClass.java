package com.mandywebdesign.impromptu.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.widget.ProgressBar;

import com.mandywebdesign.impromptu.R;
import com.yarolegovich.mp.util.Utils;

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
