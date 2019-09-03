package com.mandywebdesign.impromptu.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import com.mandywebdesign.impromptu.R;
import com.yarolegovich.mp.util.Utils;

public class ProgressBarClass {

    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.setProgressStyle(R.style.CustomFontDialog);
        m_Dialog.setCanceledOnTouchOutside(false);
        m_Dialog.show();

        Drawable drawable = new ProgressBar(context).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(context, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        m_Dialog.setIndeterminateDrawable(drawable);
        return m_Dialog;
    }
}
